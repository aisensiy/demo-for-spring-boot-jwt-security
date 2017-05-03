package com.example.api.security;

import com.example.api.UserPostsApi;
import com.example.domain.user.User;
import com.example.domain.user.UserRepository;
import com.example.service.JwtService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {UserPostsApi.class})
@Import(WebSecurityConfig.class)
public class UserPostsApiTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @Autowired
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void should_get_401_with_no_user() throws Exception {
        given()
            .when()
            .get("/users/{userId}/posts", "123")
            .then()
            .statusCode(401);
    }

    @Test
    public void should_get_200_with_valid_token() throws Exception {
        User user = new User("123", "aisensiy", "aisensiy@gmail.com", "123");
        when(userRepository.getUserByUserName(eq("aisensiy"))).thenReturn(Optional.of(user));
        when(jwtService.getUsername(anyObject())).thenReturn("aisensiy");

        given()
            .when()
            .get("/users/{userId}/posts", "123")
            .then()
            .statusCode(200);
    }

    @Test
    public void should_get_403_with_other_user() throws Exception {
        User user = new User("123", "aisensiy", "aisensiy@gmail.com", "123");
        when(userRepository.getUserByUserName(eq("aisensiy"))).thenReturn(Optional.of(user));
        when(jwtService.getUsername(anyObject())).thenReturn("aisensiy");

        given()
            .when()
            .get("/users/{userId}/posts", "456")
            .then()
            .statusCode(403);

    }
}
