package com.example.domain;

public interface EncryptService {
    String encrypt(String password);

    boolean check(String password, String encrypedPassword);
}
