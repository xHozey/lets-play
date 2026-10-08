package com.zone01.letsplay.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone01.letsplay.dto.LoginReq;
import com.zone01.letsplay.dto.RegisterReq;
import com.zone01.letsplay.service.AuthService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String Register(@RequestBody @Valid RegisterReq req) {
        authService.register(req);
        return "";
    }

    @PostMapping("/login")
    public String Login(@RequestBody @Valid LoginReq req) {
        String token = authService.login(req);
        return token;
    }
}