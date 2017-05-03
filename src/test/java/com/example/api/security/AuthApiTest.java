package com.example.api.security;

import com.example.api.AuthApi;
import com.example.domain.EncryptService;
import com.example.domain.User;
import com.example.domain.UserRepository;
import com.example.infrastructure.service.BCryptService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AuthApiTest {
    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil("123");
    private UserRepository userRepository;
    private EncryptService encryptService = new BCryptService();

    private AuthApi authApi;

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User("123", "aisensiy", "aisensiy@163.com", encryptService.encrypt("123"));
        userRepository = mock(UserRepository.class);
        when(userRepository.getUserByUserName(eq("aisensiy"))).thenReturn(Optional.of(user));
        authApi = new AuthApi(encryptService, userRepository, jwtTokenUtil);
    }

    @Test
    public void should_login_success() throws Exception {

        Map<String, Object> loginParameter = new HashMap<String, Object>() {{
            put("username", "aisensiy");
            put("password", "123");
        }};

        given()
            .standaloneSetup(authApi)
            .contentType("application/json")
            .body(loginParameter)
            .when()
            .post("/auth")
            .then()
            .statusCode(200);
    }

    @Test
    public void should_fail_login_with_wrong_password() throws Exception {
        Map<String, Object> loginParameter = new HashMap<String, Object>() {{
            put("username", "aisensiy");
            put("password", "456");
        }};

        given()
            .standaloneSetup(authApi)
            .contentType("application/json")
            .body(loginParameter)
            .when()
            .post("/auth")
            .then()
            .statusCode(400);
    }

    @Test
    public void should_fail_with_wrong_parameter_format() throws Exception {
        Map<String, Object> loginParameter = new HashMap<String, Object>() {{
            put("username", "aisensiy");
            put("passwd", "123");
        }};

        given()
            .standaloneSetup(authApi)
            .contentType("application/json")
            .body(loginParameter)
            .when()
            .post("/auth")
            .then()
            .statusCode(400);

    }
}