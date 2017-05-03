package com.example.infrastructure.service;

import com.example.domain.user.EncryptService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptService implements EncryptService {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encrypt(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean check(String password, String encryptedPassword) {
        return encoder.matches(password, encryptedPassword);
    }
}
