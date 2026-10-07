package com.zone01.letsplay.service;

import org.springframework.stereotype.Service;

import com.zone01.letsplay.dto.AuthReq;
import com.zone01.letsplay.exception.ConflictException;
import com.zone01.letsplay.exception.ResourceNotFoundException;
import com.zone01.letsplay.exception.UnauthorizedException;
import com.zone01.letsplay.model.User;
import com.zone01.letsplay.repository.UserRepo;
import com.zone01.letsplay.utils.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public String[] register(AuthReq authReq) {

        if (userRepo.existsByEmail(authReq.email())) {
            throw new ConflictException("email already exist");
        }
        if (userRepo.existsByName(authReq.name())) {
            throw new ConflictException("name already exist");
        }
        User userEntity = new User();
        userEntity.setEmail(authReq.email());
        userEntity.setName(authReq.name());
        userEntity.setPassword(encoder.encode(authReq.password()));
        userEntity.setRole("USER");
        userRepo.save(userEntity);
        return new String[] {};
    }

    public String login(AuthReq authReq) {
        User user = userRepo.findByName(authReq.name()).or(() -> userRepo.findByEmail(authReq.email()))
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        System.out.println(user.getPassword());
        if (!encoder.matches(authReq.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        String token = jwtUtil.generateAuthenticationToken(user);

        return token;
    }
}