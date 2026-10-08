package com.zone01.letsplay.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zone01.letsplay.dto.UserResponse;
import com.zone01.letsplay.dto.UserUpdateReq;
import com.zone01.letsplay.exception.ConflictException;
import com.zone01.letsplay.exception.ResourceNotFoundException;
import com.zone01.letsplay.model.User;
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

    public UserResponse UpdateUser(String id, UserUpdateReq userUpdateReq) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));

        userRepo.findByEmail(userUpdateReq.email()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new ConflictException("email already exist");
            }
        });
        userRepo.findByName(userUpdateReq.name()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new ConflictException("name already exist");
            }
        });

        user.setEmail(userUpdateReq.email());
        user.setName(userUpdateReq.name());
        User savedUser = userRepo.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    public void DeleteUser(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
        userRepo.delete(user);
    }
}