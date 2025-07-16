package com.example.marketingagencymanagement.service;

import antlr.Token;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.marketingagencymanagement.dto.LoginDto;
import com.example.marketingagencymanagement.dto.TokenDto;
import com.example.marketingagencymanagement.model.Client;
import com.example.marketingagencymanagement.model.Role;
import com.example.marketingagencymanagement.model.ServicePackageType;
import com.example.marketingagencymanagement.model.User;
import com.example.marketingagencymanagement.repository.ClientRepository;
import com.example.marketingagencymanagement.dto.*;
import com.example.marketingagencymanagement.model.*;
import com.example.marketingagencymanagement.repository.*;
import com.example.marketingagencymanagement.security.JwtUtils;
import com.example.marketingagencymanagement.security.TotpManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private CommercialRepository commercialRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;
    @Autowired
    private NotificationService notificationService;

    private TotpManager totpManager = new TotpManager();
    private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    public UserService() { }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public TokenDto login(LoginDto loginDto) {

        if(!validateRecaptcha(recaptchaSecret, loginDto.getRecaptchaToken())){
            logger.error("Captcha greska, username: "+loginDto.getUsername()+"!");
            //System.out.printf("CAPTCHA ERROR: ", loginDto.getRecaptchaToken());
            notificationService.sendToAdmins(findAllAdmins(),"Username: "+loginDto.getUsername());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message: Incorrect captcha!");
        }

        Optional<User> userOpt = userRepository.findByUsername(loginDto.getUsername());

        if (userOpt.isEmpty()) {
            logger.error("Losi kredencijali pri logovanju, username: "+loginDto.getUsername()+"!");
            notificationService.sendToAdmins(findAllAdmins(),"Username: "+loginDto.getUsername());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message: Incorrect credentials!");
        }
        try{
            if (userOpt.get().getIsBlockedUser()) {
                logger.error("Blokiran korisnik pokusao da se uloguje, username: "+loginDto.getUsername()+"!");
                notificationService.sendToAdmins(findAllAdmins(),"Username: "+loginDto.getUsername());
                throw new ResponseStatusException(HttpStatus.CONFLICT, "message: Blocked user!");
            }

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
            //System.out.println("Authorities: " + authorities);

            if(!userOpt.get().isUsing2FA()) {
                String accessToken = jwtUtils.generateAccessToken(authentication);
                String refreshToken = jwtUtils.generateRefreshToken(authentication);

                TokenDto tokens = new TokenDto();
                tokens.setAccessToken(accessToken);
                tokens.setRefreshToken(refreshToken);
                return tokens;
            }


            logger.info("Uspesno logovanje korisnika "+ loginDto.getUsername()+"!");
            return new TokenDto("", "");
        }
        catch(Exception e)
        {
            logger.info("Neuspesno logovanje korisnika "+ loginDto.getUsername()+", pogresna lozinka!");
            notificationService.sendToAdmins(findAllAdmins(),"Username: "+loginDto.getUsername());
            e.printStackTrace();
            return null;
        }

    }

    public boolean validateRecaptcha(String secretKey, String captchaResponse) {
        final String verificationUrl = "https://www.google.com/recaptcha/api/siteverify";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String bodyContent = "secret=" + secretKey + "&response=" + captchaResponse;
        HttpEntity<String> requestEntity = new HttpEntity<>(bodyContent, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> apiResponse = restTemplate.postForEntity(verificationUrl, requestEntity, String.class);

        if (apiResponse.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(apiResponse.getBody());

                return jsonResponse.path("success").asBoolean(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public TokenDto verifyTotp(String username, String code) {
        User user = userRepository.findByUsername(username).get();
        TokenDto tokens = new TokenDto();
        if(totpManager.verifyTotp(code, user.getSecret2FA())){
            String accessToken = jwtUtils.generateAccessToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(user);

            tokens.setAccessToken(accessToken);
            tokens.setRefreshToken(refreshToken);
            return tokens;
        }
        return null;
    }

    @Override
    public Boolean sendVerificationEmail(PasswordlessDto passwordlessDto) {
        if(!validateRecaptcha(recaptchaSecret, passwordlessDto.getRecaptchaToken())){
            //System.out.printf("CAPTCHA ERROR: ", passwordlessDto.getRecaptchaToken());
            logger.error("Korisnik " + passwordlessDto.getEmail() + " nije prosao Captcha test!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message: Incorrect captcha!");
        }

        Optional<User> user = userRepository.findByUsername(passwordlessDto.getEmail());
        if (user.isEmpty()) {
            //System.out.println("User not found");
            logger.error("Korisnik " + passwordlessDto.getEmail() + " nije pronadjen, losi kredencijali!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: Incorrect credentials!");
        }

        Optional<Client> client = clientRepository.findById(user.get().getId());
        if (client.isEmpty()) {
            System.out.println("Client not found");
            logger.error("Klijent " + passwordlessDto.getEmail() + " nije pronadjen!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: Client not found!");
        }

        if(client.get().getServicePackage().equals(ServicePackageType.BASIC)){
            logger.error("Klijent " + passwordlessDto.getEmail() + " je u BASIC paketu!");
            return false;
        }

        //sendEmail(username);
        logger.info("Klijentu " + passwordlessDto.getEmail() + " poslat verifikaioni mejl!");
        sendEmail(passwordlessDto.getEmail());
        return true;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public boolean hasAuthority(String jwtToken, String permissionName)
    {
        String username=jwtUtils.getUserNameFromToken(jwtToken);
        if(findByUsername(username).isPresent())
        {
            User user=findByUsername(username).get();
            if(user.hasPermission(permissionName))
                return true;
        }
        return false;
    }

    public void sendEmail(String username) {
        String verificationLink = "http://localhost:4200/passwordlessLogin/verify/id="
                + jwtUtils.generateVerificationToken(username);
        String verificationMail = generateVerificationEmail(username, verificationLink);
        emailService.sendNotificaitionAsync(username, "Mejl za passwordless login, BSEP", verificationMail);
        //System.out.println("Email poslat valjda...");
    }

    private String generateVerificationEmail(String name, String verificationLink) {
        return String.format(
                "<p>Please click the following link to log in:</p>\n" +
                        "<p><a href=" + verificationLink + ">Activation Link</a></p>\n" +
                        "<p>After 10 minutes the link will not be available anymore.</p>\n" +
                        "<p>Best regards,<br/>The BSEP Team</p>"
                , name, verificationLink);
    }

    @Override
    public String validateVerificationToken(String token) {
        try {

            if (jwtUtils.validateJwtToken(token)
                    && !jwtUtils.isTokenExpired(token)) {
                Optional<Client> client = clientRepository.findByUsername(jwtUtils.getUserNameFromToken(token));
                if (client.isEmpty() || !client.get().isEnabled()) {
                    System.out.println("User not found");
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: User not found!");
                }

                if(client.get().getVerificationToken() != null && client.get().getVerificationToken().equals(token)){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message: Verifaction link can be used " +
                            "only once!");
                }
                client.get().setVerificationToken(token);
                userRepository.save(client.get());
                return jwtUtils.getUserNameFromToken(token);
            }
            return null;
        }catch (TokenExpiredException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: The link has expired!");
        }
    }


    @Override
    public TokenDto passwordlessLogin(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            //System.out.println("User not found");
            logger.error("Korisnik " + username + " nije pronadjen, losi kredencijali!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: User not found!");
        }

        String accessToken = jwtUtils.generateAccessToken(user.get());
        String refreshToken = jwtUtils.generateRefreshToken(user.get());

        TokenDto tokens = new TokenDto();
        tokens.setAccessToken(accessToken);
        tokens.setRefreshToken(refreshToken);
        logger.info("Korisnik " + username + " uspesno ulogovan pomocu passwordlessLogin metode!");
        return tokens;
    }

    @Override
    public TokenDto refreshToken(String refreshToken) {
        String username = jwtUtils.getUserNameFromToken(refreshToken);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty() || user.get().getIsBlockedUser()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: User not found or blocked!");
        }

        String newAccessToken = jwtUtils.generateAccessToken(user.get());
        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(newAccessToken);
        tokenDto.setRefreshToken(refreshToken);

        return tokenDto;
    }
    public ArrayList<User> findInactiveClients() {
        ArrayList<User> inactiveClients = userRepository.findByRolesNameAndEnabledIsFalseAndActivationPendingIsFalse("client");
        ArrayList<User> result = new ArrayList<>();

        for (User user : inactiveClients) {
            if (user.getBlocked() == null || user.getBlocked().isBefore(LocalDateTime.now())) {
                result.add(user);
            }
        }

        return result;
    }
    @Override
    public Boolean sendMailToActivate(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            //System.out.println("User not found");
            logger.error("Korisnik " + username + " nije pronadjen, losi kredencijali!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: Incorrect credentials!");
        }
        user.get().setActivationPending(true);
        userRepository.save(user.get());
        sendActivationEmail(username);
        return true;
    }

    public void sendActivationEmail(String username) {
        String activationLink = "http://localhost:4200/accountActivation/id="
                + jwtUtils.generateActivationToken(username);
        String verificationMail = generateActivationEmail(username, activationLink);
        emailService.sendNotificaitionAsync(username, "Activation link, BSEP", verificationMail);
    }
    private String generateActivationEmail(String username, String link) {
        return String.format(
                "<p>Please click the following link to activate your account:</p>\n" +
                        "<p><a href=" + link + ">Activation Link</a></p>\n" +
                        "<p>After 24 hours the link will not be available anymore.</p>\n" +
                        "<p>Best regards,<br/>The BSEP Team</p>"
                , username, link);
    }

    @Override
    public Boolean validateActivationToken(String token) {
        if (jwtUtils.validateJwtToken(token)){
            Optional<User> user = userRepository.findByUsername(jwtUtils.getUserNameFromToken(token));
            if (user.isEmpty()) {
                System.out.println("User not found");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: User not found!");
            }
            if (user.get().isEnabled()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User account is already activated!");
            }
            user.get().setEnabled(true);
            user.get().setActivationPending(false);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    public Boolean blockUser(String username, String reason)
    {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            //System.out.println("User not found");
            logger.error("Korisnik " + username + " nije pronadjen, losi kredencijali!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: User not found!");
        }
        LocalDateTime now = LocalDateTime.now();
        user.get().setBlocked(now.plusHours(24));
        userRepository.save(user.get());
        String blockMail = generateBlockEmail(username, reason);
        emailService.sendNotificaitionAsync(username, "The registration request has been denied", blockMail);
        logger.info("Korisnik " + username + " je blokiran!");
        return true;
    }
    private String generateBlockEmail(String username, String reason) {
        return String.format(
                "<p>We're sorry, but your registration request has been denied for the following reason: " + reason + ". You cannot register for the next 24 hours.</p>\n" +
                        "<p>Best regards,<br/>The BSEP Team</p>"
                , reason);
    }
    @Override
    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(() -> new NoSuchElementException("User not found"));
        return user;
    }

    public User updateUser(UserProfileDto dto) throws Exception{
        User user = new User(dto);
        if ((user.getId() == null)){
            throw new Exception("ID must not be null for updating entity.");
        }
        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public boolean changePassword(ChangePasswordDto password){
        Optional<User> optionalUser = findByUsername(password.username);
        try {
            if(optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (passwordEncoder.matches(password.oldPassword, user.getPassword()) && password.newPassword.equals(password.repeatedPassword)) {
                    user.setPassword(passwordEncoder.encode(password.newPassword));
                    userRepository.save(user);
                    Optional<Employee> optionalEmployee = employeeRepository.findById(user.getId());
                    if(!optionalEmployee.isEmpty()){
                        Employee employee = optionalEmployee.get();
                        employee.setHasChangedPassword(true);
                        employeeRepository.save(employee);
                        logger.info("Korisnik "+password.username+" je uspesno promenio lozinku!");
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Korisnik " + password.username + " nije pronadjen!");
        }
        return false;
    }

    public ArrayList<User> findByRolesName(String roleName) {
        return userRepository.findByRolesName(roleName);
    }

    public User createUser(UserCreationDto dto) throws Exception {
        User user = new User(dto);
        user.setName(dto.name);
        user.setSurname(dto.surname);
        user.setUsername(dto.username);
        user.setPassword(passwordEncoder.encode(dto.password));
        user.setAddress(dto.address);
        user.setCity(dto.city);
        user.setCountry(dto.country);
        user.setPhone(dto.phone);
        user.setEnabled(true);
        user.setIsBlockedUser(false);
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRole = roleRepository.findByName(dto.role);

        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            roles.add(role);
            user.setRoles(roles);
        }

        if (user.getId() != null) {
            logger.error("Korisnik " + user.getUsername() + " nije napravljen!!");
        }

        User savedUser = userRepository.save(user);
        logger.info("Korisnik " + user.getUsername() + " je napravljen!!");
        return savedUser;
    }

    public Request createRequest(RequestCreationDto dto) throws Exception {
        Request request = new Request();
        Optional<Client> optionalClient = clientRepository.findById(Long.parseLong(dto.getClientId()));
        if(optionalClient.isPresent()){
            Client client = optionalClient.get();
            request.setClient(client);
        }
        request.setActiveFrom(dto.getActiveFrom());
        request.setActiveTo(dto.getActiveTo());
        request.setDeadline(dto.getDeadline());
        request.setDescription(dto.getDescription());

        logger.info("Zahtev je napravljena!");
        if (request.getId() != null) {
            logger.error("Zahtev nije napravljena!");
            throw new Exception("ID must be null for a new entity.");
        }

        Request savedRequest = requestRepository.save(request);
        return savedRequest;
    }

    @Override
    public ArrayList<Request> findAllRequestsNotInPast() throws ParseException {
        Date currentDate = new Date();
        List<Request> allRequests = requestRepository.findAll();
        ArrayList<Request> requestsNotInPast = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        for (Request request : allRequests) {
            Date date = formatter.parse(request.getDeadline());
            if (!date.before(currentDate)) {
                requestsNotInPast.add(request);
            }
        }

        return requestsNotInPast;
    }
    public ArrayList<Commercial> findAllCommercials(){
        return  commercialRepository.findAll();
    }

    public Commercial createCommercial(CommercialDto dto) throws Exception {
        Commercial commercial = new Commercial();

        ClientDto clientDto = dto.getClient();
        Client client = new Client();
        Optional<Client> populatedClient = clientRepository.findByUsername(clientDto.getUsername());
        if(populatedClient.isPresent()){
            client = populatedClient.get();
            commercial.setClient(client);
        }

        EmployeeDto employeeDto = dto.getEmployee();
        Employee employee = new Employee();
        Optional<Employee> populatedEmployee = employeeRepository.findById(employeeDto.getId());
        if(populatedEmployee.isPresent()){
            employee = populatedEmployee.get();
            commercial.setEmployee(employee);
        }

        commercial.setDuration(dto.getDuration());
        commercial.setMoto(dto.getMoto());
        commercial.setDescription(dto.getDescription());
        logger.info("Reklama je napravljena!");

        if (commercial.getId() != null) {
            logger.error("Reklama nije napravljena!");
            throw new Exception("ID must be null for a new entity.");
        }

        Commercial savedCommercial = commercialRepository.save(commercial);
        return savedCommercial;
    }

    public void deleteRequest(Long id){
        requestRepository.deleteById(id);
        logger.info("Zahtev sa ID-jem: "+id+" je obrisan!");
    }

    @Override
    public Employee findEmployeeById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        Employee employee = employeeOptional.orElseThrow(() -> new NoSuchElementException("Employee not found"));
        return employee;
    }

    public List<User> findAllAdmins()
    {
        List<User> admins=userRepository.findByRolesName("admin");
        return admins;
    }

    @Override
    public Boolean blockUser(String username)
    {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            //System.out.println("User not found");
            logger.error("Korisnik " + username+ " nije pronadjen. Losi kredencijali!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: User not found!");
        }
        user.get().setIsBlockedUser(true);
        userRepository.save(user.get());
        logger.info("Korisnik " + username + " je blokiran!");
        return user.get().getIsBlockedUser();
    }

    @Override
    public Boolean sendMailForForgottenPassword(PasswordlessDto dto) {
        if(!validateRecaptcha(recaptchaSecret, dto.getRecaptchaToken())){
            System.out.printf("CAPTCHA ERROR: ", dto.getRecaptchaToken());
            logger.error("Korisnik " + dto.getEmail() + " nije prosao Captcha test!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message: Incorrect captcha!");
        }

        Optional<User> user = userRepository.findByUsername(dto.getEmail());
        if (user.isEmpty()) {
            logger.error("Korisnik " + dto.getEmail() + " nije pronadjen. Losi kredencijali!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: Incorrect credentials!");
        }

        if(!user.get().getIsBlockedUser()) {
            sendEmailToChangePassword(dto.getEmail());
            return true;
        }
        return false;
}

    public void sendEmailToChangePassword(String username) {
        System.out.println("sendEmailToChangePassword");
        String activationLink = "http://localhost:4200/newPassword/id="
                + jwtUtils.generateActivationToken(username);
        String verificationMail = generatePasswordChangeEmail(username, activationLink);
        emailService.sendNotificaitionAsync(username, "Change password link, BSEP", verificationMail);
    }

    private String generatePasswordChangeEmail(String username, String link) {
        return String.format(
                "<p>Please click the following link to change your password:</p>\n" +
                        "<p><a href=" + link + ">Activation Link</a></p>\n" +
                        "<p>After 24 hours the link will not be available anymore.</p>\n" +
                        "<p>Best regards,<br/>The BSEP Team</p>"
                , username, link);
    }

    @Override
    public TokenDto createNewPasswordIfForgotten(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            //System.out.println("User not found");
            logger.error("Korisnik " + username + " nije pronadjen, losi kredencijali!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: User not found!");
        }

        String accessToken = jwtUtils.generateAccessToken(user.get());
        String refreshToken = jwtUtils.generateRefreshToken(user.get());

        TokenDto tokens = new TokenDto();
        tokens.setAccessToken(accessToken);
        tokens.setRefreshToken(refreshToken);
        logger.info("Korisnik " + username + " je uspesno promenio lozinku!");
        return tokens;
    }

    public boolean recoverAccount(RecoverAccountDto credentials){
        if(validateForgottenPasswordToken(credentials.token)){
            Optional<User> optionalUser = userRepository.findByUsername(jwtUtils.getUserNameFromToken(credentials.token));
            //System.out.println(optionalUser.get().getUsername());
            try {
                if(optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    user.setPassword(passwordEncoder.encode(credentials.newPassword));
                    userRepository.save(user);
                    logger.info("Korisnik " + user.getUsername() + " je uspesno povratio nalog!");
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Korisnik nije pronadjen, losi kredencijali!");
            }
        }
        return false;
    }

    public boolean deleteAccount(String username) {
        Optional<User> optionalUser = findByUsername(username);
        Optional<Client> client = clientRepository.findByUsername(username);
        if (client.isEmpty() || !client.get().isEnabled()) {
            logger.error("Korisnik " + username + " nije pronadjen, losi kredencijali!");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: User not found!");
        }

        try {
            if(optionalUser.isPresent() && client.get().getServicePackage().equals(ServicePackageType.GOLD)) {
                userRepository.deleteById(optionalUser.get().getId());
                logger.info("Korisnik " + username + " je uspesno izbrisao profil!");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean validateForgottenPasswordToken(String token) {
        if (jwtUtils.validateJwtToken(token)){
            Optional<User> user = userRepository.findByUsername(jwtUtils.getUserNameFromToken(token));
            if (user.isEmpty()) {
                logger.error("Korisnik nije pronadjens!");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "message: User not found!");
            }
            return true;
        }
        return false;
    }
}