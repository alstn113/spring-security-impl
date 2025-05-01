package com.alstn113.security.app.ui;

import com.alstn113.security.security.authentication.JwtAuthentication;
import com.alstn113.security.security.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "모두 접근 가능";
    }

    @GetMapping("/api/posts")
    public String getPosts() {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();

        return "인증된 사용자: 게시물 조회 #" + authentication.principal();
    }
}
