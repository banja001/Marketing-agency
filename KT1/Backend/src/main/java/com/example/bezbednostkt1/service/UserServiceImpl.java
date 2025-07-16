package com.example.bezbednostkt1.service;

import com.example.bezbednostkt1.dto.LoginDto;
import com.example.bezbednostkt1.dto.UserDto;
import com.example.bezbednostkt1.dto.UserTokenState;
import com.example.bezbednostkt1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.bezbednostkt1.model.User;
import org.springframework.web.server.ResponseStatusException;
import com.example.bezbednostkt1.security.JwtUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserTokenState login(LoginDto loginDto) {

        Optional<User> userOpt = userRepository.findByUsername(loginDto.getUsername());
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message: Incorrect credentials!");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        User user = (User) authentication.getPrincipal();

        UserTokenState tokenDTO = new UserTokenState();
        tokenDTO.setAccessToken(jwt);
        tokenDTO.setExpiresIn(10000000L);

        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        System.out.println("Authorities: " + authorities);

        return tokenDTO;
    }

    public UserDto findByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        User user = userOpt.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect username!"));

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());
        userDto.setOrganization(user.getOrganization());
        userDto.setCountry(user.getCountry());

        return userDto;
    }

}
