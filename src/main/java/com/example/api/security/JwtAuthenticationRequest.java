package com.example.api.security;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class JwtAuthenticationRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
