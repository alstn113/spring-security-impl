package com.alstn113.security.security.authentication;

import com.alstn113.security.security.authorization.GrantedAuthority;
import com.alstn113.security.security.context.Authentication;
import java.util.ArrayList;
import java.util.Collection;

public class JwtAuthentication implements Authentication {

    private final Long memberId;
    private final Collection<GrantedAuthority> authorities;

    public JwtAuthentication(Long memberId, Collection<? extends GrantedAuthority> authorities) {
        this.memberId = memberId;
        this.authorities = new ArrayList<>(authorities);
    }

    @Override
    public Long principal() {
        return memberId;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>(authorities);
    }

    @Override
    public String toString() {
        return "%s: [ memberId= %s ]".formatted(getClass().getSimpleName(), memberId);
    }
}
