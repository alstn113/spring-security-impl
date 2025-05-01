package com.alstn113.security;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.alstn113.security.app.application.AuthService;
import com.alstn113.security.app.application.request.LoginRequest;
import com.alstn113.security.app.application.request.RegisterRequest;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

    private static final String MEMBER_USERNAME = "member_username";
    private static final String MEMBER_PASSWORD = "member_password";
    private static final String ADMIN_USERNAME = "admin_username";
    private static final String ADMIN_PASSWORD = "admin_password";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeAll
    static void registerTestUsers(@Autowired AuthService authService) {
        authService.register(new RegisterRequest(MEMBER_USERNAME, MEMBER_PASSWORD, "MEMBER"));
        authService.register(new RegisterRequest(ADMIN_USERNAME, ADMIN_PASSWORD, "ADMIN"));
    }

    @Test
    @DisplayName("'/public'은 접근 가능하다.")
    void testPublicEndpoint() {
        given().log().all()
                .get("/public")
                .then().log().all()
                .statusCode(200)
                .body(equalTo("모두 접근 가능"));
    }

    @Test
    @DisplayName("'/api/posts' 요청은 인증된 사용자는 접근 가능하다.")
    void testGetPosts() {
        Cookie cookie = getMemberAccessTokenCookie();

        given().log().all()
                .cookie(cookie)
                .get("/api/posts")
                .then().log().all()
                .statusCode(200)
                .body(equalTo("인증된 사용자: 게시물 조회 #1"));
    }

    @Test
    @DisplayName("'/api/posts' 요청은 인증되지 않은 사용자는 401을 반환한다.")
    void testGetPostsWithoutAuthentication() {
        given().log().all()
                .get("/api/posts")
                .then().log().all()
                .statusCode(401);
    }

    private Cookie getMemberAccessTokenCookie() {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginRequest(MEMBER_USERNAME, MEMBER_PASSWORD))
                .post("/login")
                .getDetailedCookie("access_token");
    }

    private Cookie getAdminAccessTokenCookie() {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginRequest(ADMIN_USERNAME, ADMIN_PASSWORD))
                .post("/login")
                .getDetailedCookie("access_token");
    }
}
