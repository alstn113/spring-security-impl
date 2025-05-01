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
    @DisplayName("'/public'은 securityFilterChain의 대상 경로가 아니므로 접근 가능하다.")
    void testPublicEndpoint() {
        given().log().all()
                .get("/public")
                .then().log().all()
                .statusCode(200)
                .body(equalTo("모두 접근 가능"));
    }

    @Test
    @DisplayName("'/api/posts'의 GET 요청은 인증 없이 접근 가능하다.")
    void testGetPosts() {
        given().log().all()
                .get("/api/posts")
                .then().log().all()
                .statusCode(200)
                .body(equalTo("인증되지 않은 사용자: 게시물 조회"));
    }

    @Test
    @DisplayName("'/api/posts'의 GET 요청은 인증된 사용자도 접근 가능하다.")
    void testGetPostsWithAuthentication() {
        Cookie cookie = getMemberAccessTokenCookie();

        given().log().all()
                .cookie(cookie)
                .get("/api/posts")
                .then().log().all()
                .statusCode(200)
                .body(equalTo("인증된 사용자: 게시물 조회 #1"));
    }

    @Test
    @DisplayName("'/api/posts'의 POST 요청은 인증되지 않은 사용자의 경우 401을 반환한다.")
    void testPostPostsWithoutAuthentication() {
        given().log().all()
                .post("/api/posts")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("'/api/posts'의 POST 요청은 인증된 사용자만 접근 가능하다.")
    void testPostPosts() {
        Cookie cookie = getMemberAccessTokenCookie();

        given().log().all()
                .cookie(cookie)
                .post("/api/posts")
                .then().log().all()
                .statusCode(200)
                .body(equalTo("인증된 사용자: 게시물 생성 #1"));
    }

    @Test
    @DisplayName("'/api/member', 인증이 필요한 요청에서 토큰이 유효하지 않은 경우, 401을 반환한다.")
    void testPrivateMemberHolderWithInvalidToken() {
        given().log().all()
                .cookie("access_token", "invalid_token")
                .get("/api/private/member")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("'/api/member', 인증이 필요한 요청에서 쿠키가 없는 경우(인증이 없는 경우), 401을 반환한다.")
    void testPrivateMemberHolderWithoutAuthentication() {
        given().log().all()
                .get("/api/private/member")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("'/api/private/member'는 MEMBER의 경우 접근 가능하다.")
    void testPrivateMember() {
        Cookie cookie = getMemberAccessTokenCookie();

        given().log().all()
                .cookie(cookie)
                .get("/api/private/member")
                .then().log().all()
                .statusCode(200)
                .body(equalTo("인증된 사용자 #1"));
    }

    @Test
    @DisplayName("'/api/private/member'는 MEMBER가 아닌 경우, 403을 반환한다.")
    void testPrivateMemberNotAuthorized() {
        Cookie cookie = getAdminAccessTokenCookie();

        given().log().all()
                .cookie(cookie)
                .get("/api/private/member")
                .then().log().all()
                .statusCode(403);
    }

    @Test
    @DisplayName("'/api/private/member/holder'는 MEMBER의 경우 접근 가능하다.")
    void testPrivateMemberHolder() {
        Cookie cookie = getMemberAccessTokenCookie();

        given().log().all()
                .cookie(cookie)
                .get("/api/private/member/holder")
                .then().log().all()
                .statusCode(200)
                .body(equalTo("인증된 사용자 #1"));
    }

    @Test
    @DisplayName("'/api/private/admin'는 ADMIN의 경우 접근 가능하다.")
    void testPrivateAdmin() {
        Cookie cookie = getAdminAccessTokenCookie();

        given().log().all()
                .cookie(cookie)
                .get("/api/private/admin")
                .then().log().all()
                .statusCode(200)
                .body(equalTo("인증된 관리자 #2"));
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
