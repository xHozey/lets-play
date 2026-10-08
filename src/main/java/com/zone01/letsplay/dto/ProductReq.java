package com.zone01.letsplay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductReq(@NotBlank @Size(min = 3, max = 600) String name,
        @NotBlank @Size(min = 50, max = 4000) String description, @Positive @NotNull Double price) {

}