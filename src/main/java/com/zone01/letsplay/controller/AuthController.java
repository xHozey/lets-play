package com.zone01.letsplay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone01.letsplay.dto.LoginReq;
import com.zone01.letsplay.dto.LoginRes;
import com.zone01.letsplay.dto.RegisterReq;
import com.zone01.letsplay.dto.UserResponse;
import com.zone01.letsplay.service.AuthService;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @PermitAll
    public UserResponse Register(@RequestBody @Valid RegisterReq req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    @PermitAll
    public LoginRes Login(@RequestBody @Valid LoginReq req) {
        String token = authService.login(req);
        return new LoginRes(token);
    }
}