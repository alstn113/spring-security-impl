package com.alstn113.security.security.authentication;

import com.alstn113.security.app.application.AuthService;
import com.alstn113.security.app.application.TokenProvider;
import com.alstn113.security.app.application.response.MemberInfoResponse;
import com.alstn113.security.security.context.Authentication;
import com.alstn113.security.security.context.SecurityContextHolder;
import com.alstn113.security.security.exception.AuthenticationEntryPoint;
import com.alstn113.security.security.exception.AuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final TokenResolver tokenResolver;
    private final AuthService authService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            Authentication authentication = attemptAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            authenticationEntryPoint.commence(request, response, e);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private Authentication attemptAuthentication(HttpServletRequest request) {
        Optional<String> tokenOpt = tokenResolver.extractAccessToken(request);
        if (tokenOpt.isEmpty()) {
            throw new AuthenticationException("액세스 토큰 쿠키가 비어있습니다.");
        }

        Long memberId = tokenProvider.getMemberId(tokenOpt.get());
        MemberInfoResponse memberInfo = authService.getMemberInfo(memberId);

        return new JwtAuthentication(memberInfo.memberId());
    }
}
