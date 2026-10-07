package com.zone01.letsplay.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zone01.letsplay.dto.UserResponse;
import com.zone01.letsplay.repository.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public List<UserResponse> GetUsers() {
        return userRepo.findAll().stream().map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }
}