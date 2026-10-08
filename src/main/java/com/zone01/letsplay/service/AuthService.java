package com.zone01.letsplay.service;

import org.springframework.stereotype.Service;

import com.zone01.letsplay.dto.LoginReq;
import com.zone01.letsplay.dto.LoginRes;
import com.zone01.letsplay.dto.RegisterReq;
import com.zone01.letsplay.dto.UserResponse;
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

    public UserResponse register(RegisterReq authReq) {

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
        User user = userRepo.save(userEntity);
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public String login(LoginReq authReq) {
        User user = userRepo.findByEmail(authReq.email())
                .orElseThrow(() -> new ResourceNotFoundException("email doesn't exists"));
        if (!encoder.matches(authReq.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        String token = jwtUtil.generateAuthenticationToken(user);

        return token;
    }
}