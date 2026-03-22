package com.fondationdelmas.training.dtos;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        boolean active,
        LocalDateTime createdAt
) {}
