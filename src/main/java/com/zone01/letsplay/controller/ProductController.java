package com.zone01.letsplay.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

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

    @DeleteMapping("/{id}")
    public Map<String, String> DeleteProduct(@PathVariable String id) {
        productService.DeleteProduct(id);
        return Map.of("message", "Product deleted succesfully");
    }
}