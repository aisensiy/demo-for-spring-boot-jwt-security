package com.example.api;

import com.example.domain.user.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("/users/{userId}/posts")
public class UserPostsApi {

    @GetMapping
    public List<Map<String, Object>> getPosts(@PathVariable("userId") String userId,
                                              @AuthenticationPrincipal User currentUser) {
        if (!currentUser.getId().equals(userId)) {
            throw new AccessDeniedException("");
        }
        return asList(
            new HashMap<String, Object>() {{
                put("id", "123");
                put("title", "title");
            }}
        );
    }
}
