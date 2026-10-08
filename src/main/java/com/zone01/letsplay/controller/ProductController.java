package com.zone01.letsplay.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone01.letsplay.dto.ProductReq;
import com.zone01.letsplay.dto.ProductRes;
import com.zone01.letsplay.service.ProductService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductRes> GetProducts() {
        return productService.GetProducts();
    }

    @PostMapping
    public ProductRes CreateProduct(@RequestBody @Valid ProductReq productReq) {
        return productService.CreateProduct(productReq);
    }

    @PutMapping("/{id}")
    public ProductRes UpdateProduct(@PathVariable String id, @RequestBody @Valid ProductReq productReq) {
        return productService.UpdateProduct(id, productReq);
    }
}