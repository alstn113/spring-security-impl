package com.alstn113.security.security.authentication;

import com.alstn113.security.security.context.Authentication;

public class JwtAuthentication implements Authentication {

    private final Long memberId;

    public JwtAuthentication(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public Long principal() {
        return memberId;
    }

    @Override
    public String toString() {
        return "%s: [ memberId= %s ]".formatted(getClass().getSimpleName(), memberId);
    }
}
