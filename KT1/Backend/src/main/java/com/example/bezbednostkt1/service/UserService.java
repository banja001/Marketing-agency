package com.example.bezbednostkt1.service;

import com.example.bezbednostkt1.dto.LoginDto;
import com.example.bezbednostkt1.dto.UserDto;
import com.example.bezbednostkt1.dto.UserTokenState;
import com.example.bezbednostkt1.model.User;
import java.util.Optional;

public interface UserService {
    public UserTokenState login(LoginDto loginDto);
    public UserDto findByUsername(String username);

}