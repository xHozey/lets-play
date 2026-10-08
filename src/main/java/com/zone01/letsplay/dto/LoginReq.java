package com.zone01.letsplay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginReq(@Email @NotBlank @Size(min = 3, max = 999) String email,
        @NotBlank() @Size(min = 6, max = 260) String password) {
}