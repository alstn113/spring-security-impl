package com.alstn113.security.app.application;

public interface TokenProvider {

    String generateToken(Long memberId);

    Long getMemberId(String token);
}
