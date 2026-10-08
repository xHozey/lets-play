package com.zone01.letsplay.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.zone01.letsplay.dto.ProductReq;
import com.zone01.letsplay.dto.ProductRes;
import com.zone01.letsplay.model.Product;
import com.zone01.letsplay.model.User;
import com.zone01.letsplay.repository.ProductRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;

    public List<ProductRes> GetProducts() {
        return productRepo.findAll().stream()
                .map(prd -> new ProductRes(prd.getId(), prd.getName(), prd.getDescription(), prd.getPrice())).toList();
    }

    public ProductRes CreateProduct(ProductReq productReq) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = new Product();
        product.setDescription(productReq.description());
        product.setName(productReq.name());
        product.setPrice(productReq.price());
        product.setUserId(user.getId());

        Product prd = productRepo.save(product);
        return new ProductRes(prd.getId(), prd.getName(), prd.getDescription(), prd.getPrice());
    }
}