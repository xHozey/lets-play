package com.zone01.letsplay.repository;

import com.zone01.letsplay.model.User;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
}