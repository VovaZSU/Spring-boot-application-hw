package com.example.springbootapplication.controller;

import com.example.springbootapplication.dto.user.UserLoginRequestDto;
import com.example.springbootapplication.dto.user.UserLoginResponseDto;
import com.example.springbootapplication.dto.user.UserRegistrationRequestDto;
import com.example.springbootapplication.dto.user.UserResponseDto;
import com.example.springbootapplication.exception.RegistrationException;
import com.example.springbootapplication.security.AuthenticationService;
import com.example.springbootapplication.service.user.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserServiceImpl userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public UserResponseDto registerUser(@Valid @RequestBody UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
