package com.example.api.security;

import com.example.DemoForSpringBootJwtSecurityApplication;
import com.example.domain.User;
import com.example.infrastructure.service.BCryptService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {DemoForSpringBootJwtSecurityApplication.class, WebSecurityConfig.class})
public class AuthApiTest {
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    WebApplicationContext webApplicationContext;

    private User user;

    @Before
    public void setUp() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(springSecurityFilterChain).build();
        RestAssuredMockMvc.mockMvc(mockMvc);
        user = new User("123", "aisensiy", "aisensiy@163.com", new BCryptService().encrypt("123"));
        when(userDetailsService.loadUserByUsername(eq("aisensiy"))).thenReturn(JwtUser.createFromUser(user));
        when(jwtTokenUtil.generateToken(anyObject())).thenReturn("token");
        when(jwtTokenUtil.getUsername(anyString())).thenReturn("aisensiy");
    }

    @Test
    public void should_login_success() throws Exception {

        Map<String, Object> loginParameter = new HashMap<String, Object>() {{
            put("username", "aisensiy");
            put("password", "123");
        }};

        given()
            .contentType("application/json")
            .body(loginParameter)
            .when()
            .post("/auth")
            .then()
            .statusCode(200)
            .body("token", equalTo("token"));
    }

    @Test
    public void should_fail_login_with_wrong_password() throws Exception {

        Map<String, Object> loginParameter = new HashMap<String, Object>() {{
            put("username", "aisensiy");
            put("password", "456");
        }};

        given()
            .contentType("application/json")
            .body(loginParameter)
            .when()
            .post("/auth")
            .then()
            .statusCode(401);
    }
}