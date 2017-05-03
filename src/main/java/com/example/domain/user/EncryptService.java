package com.example.domain.user;

public interface EncryptService {
    String encrypt(String password);

    boolean check(String password, String encryptedPassword);
}
