package com.alstn113.security.security.authorization;

import java.util.Collection;

public class AuthorityAuthorizationDecision extends AuthorizationDecision {

    private final Collection<GrantedAuthority> authorities;

    public AuthorityAuthorizationDecision(boolean granted, Collection<GrantedAuthority> authorities) {
        super(granted);
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "%s: [ granted= %s, authorities= %s ]".formatted(getClass().getSimpleName(), isGranted(), authorities);
    }
}
