package com.example.api;

import com.example.api.security.JwtAuthenticationRequest;
import com.example.api.security.JwtTokenUtil;
import com.example.api.security.JwtUser;
import com.example.domain.EncryptService;
import com.example.domain.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private JwtTokenUtil jwtTokenUtil;

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    public AuthApi(EncryptService encryptService, UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.encryptService = encryptService;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody JwtAuthenticationRequest jwtAuthenticationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("bad request");
        }

        return userRepository.getUserByUserName(jwtAuthenticationRequest.getUsername()).map(user -> {
            if (encryptService.check(jwtAuthenticationRequest.getPassword(), user.getPassword())) {
                String token = jwtTokenUtil.generateToken(JwtUser.createFromUser(user));

                return ResponseEntity.ok(new HashMap<String, Object>() {{
                    put("token", token);
                }});
            } else {
                return ResponseEntity.badRequest().build();
            }

        }).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
