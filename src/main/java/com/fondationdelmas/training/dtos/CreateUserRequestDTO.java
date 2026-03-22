package com.fondationdelmas.training.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDTO(
        @Email @NotBlank String email,
        @NotBlank String firstName,
        @NotBlank String lastName
) {}