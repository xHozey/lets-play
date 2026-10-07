package com.zone01.letsplay.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.zone01.letsplay.model.Product;

@Repository
public interface ProductRepo extends MongoRepository<Product, String> {

}