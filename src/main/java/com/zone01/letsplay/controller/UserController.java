package com.zone01.letsplay.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone01.letsplay.dto.UserResponse;
import com.zone01.letsplay.dto.UserUpdateReq;
import com.zone01.letsplay.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> GetUsers() {
        return userService.GetUsers();
    }

    @GetMapping("/{id}")
    @PostAuthorize("returnObject.id == authentication.principal.id or hasRole('ADMIN')")
    public UserResponse GetUser(@PathVariable String id) {
        return userService.GetUser(id);
    }

    @PutMapping("/{id}")
    public UserResponse UpdateUser(@PathVariable String id, @RequestBody @Valid UserUpdateReq userUpdateReq) {
        return userService.UpdateUser(id, userUpdateReq);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> DeleteUser(@PathVariable String id) {
        userService.DeleteUser(id);
        return Map.of("message", "User deleted successfully");
    }
}