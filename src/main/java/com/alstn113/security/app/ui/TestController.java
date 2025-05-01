package com.alstn113.security.app.ui;

import com.alstn113.security.security.authentication.JwtAuthentication;
import com.alstn113.security.security.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        if (authentication == null) {
            return "인증되지 않은 사용자: 게시물 조회";
        }
        return "인증된 사용자: 게시물 조회 #" + authentication.principal();
    }

    @PostMapping("/api/posts")
    public String createPost() {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return "인증된 사용자: 게시물 생성 #" + authentication.principal();
    }

    @GetMapping("/api/private/member")
    public String privateMember() {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return "인증된 사용자 #" + authentication.principal();
    }

    @GetMapping("/api/private/admin")
    public String privateAdmin() {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return "인증된 관리자 #" + authentication.principal();
    }
}
