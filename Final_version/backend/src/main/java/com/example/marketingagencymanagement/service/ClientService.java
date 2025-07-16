package com.example.marketingagencymanagement.service;

import com.example.marketingagencymanagement.dto.ClientDto;
import com.example.marketingagencymanagement.exception.UsernameAlreadyExistsException;
import com.example.marketingagencymanagement.model.Client;
import com.example.marketingagencymanagement.model.Role;
import com.example.marketingagencymanagement.model.User;
import com.example.marketingagencymanagement.repository.ClientRepository;
import com.example.marketingagencymanagement.repository.RoleRepository;
import com.example.marketingagencymanagement.security.TotpManager;
import dev.samstevens.totp.exceptions.QrGenerationException;
import com.example.marketingagencymanagement.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.marketingagencymanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClientService implements IClientService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private TotpManager totpManager = new TotpManager();

    private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    @Override
    public String create(ClientDto clientDto) throws QrGenerationException {
        {
            Optional<User> userExists = userRepository.findByUsername(clientDto.getUsername());
            if (userExists.isPresent()) {
                logger.error("Klijent "+clientDto.getUsername()+" vec postoji!");
                throw new UsernameAlreadyExistsException("Username already exists.");
            }

            String secret2FA = totpManager.generateSecretKey();

            Client client = new Client(clientDto);
            client.setPassword(passwordEncoder.encode(clientDto.getPassword()));
            client.setSecret2FA(secret2FA);
            Role clientRole = roleRepository.findByName("client")
                    .orElseThrow(() -> new RuntimeException("Role client not found"));
            List<Role> roles = new ArrayList<>();
            roles.add(clientRole);
            client.setRoles(roles);
            Client savedClient = clientRepository.save(client);
            logger.info("Klijent "+clientDto.getUsername()+" je uspesno kreiran!");

            if (clientDto.getUsing2FA()) {
                String qrCode = totpManager.getQRCode(client.getSecret2FA(), client.getUsername());
                return qrCode;
            } else {
                return "";
            }
        }
    }

}
