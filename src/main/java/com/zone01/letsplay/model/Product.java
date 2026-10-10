package com.zone01.letsplay.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    @Field("id")
    private String id;
    @Field("name")
    private String name;
    @Field("description")
    private String description;
    @Field("price")
    private Double price;
    @Field("userId")
    private String userId;
}