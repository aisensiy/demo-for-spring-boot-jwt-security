package com.example.api.security;

import com.example.service.JwtService;
import com.example.domain.user.EncryptService;
import com.example.domain.user.UserRepository;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

@RequestMapping("/auth")
@RestController
public class AuthApi {

    private EncryptService encryptService;
    private UserRepository userRepository;
    private JwtService jwtService;

    @Autowired
    public AuthApi(EncryptService encryptService, UserRepository userRepository, JwtService jwtService) {
        this.encryptService = encryptService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("bad request");
        }

        return userRepository.getUserByUserName(authRequest.getUsername()).map(user -> {
            if (encryptService.check(authRequest.getPassword(), user.getPassword())) {
                String token = jwtService.generateToken(user);

                return ResponseEntity.ok(new HashMap<String, Object>() {{
                    put("token", token);
                }});
            } else {
                return ResponseEntity.badRequest().build();
            }

        }).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Data
    public static class AuthRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }
}
