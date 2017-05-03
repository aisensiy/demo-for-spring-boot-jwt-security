package com.example.api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
@NoArgsConstructor
public class JwtTokenUtil implements Serializable {
    @Value("${jwt.secret}")
    private String secret;

    public JwtTokenUtil(String secret) {
        this.secret = secret;
    }

    public String getUsername(String authToken) {
        if (authToken == null) {
            return null;
        }

        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody().getSubject();
        } catch (SignatureException e) {
            return null;
        }
    }

    public String generateToken(JwtUser jwtUser) {
        Map<String, Object> claims = new HashMap<String, Object>() {{
            put("sub", jwtUser.getUsername());
            put("id", jwtUser.getId());
            put("email", jwtUser.getEmail());
        }};

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}
