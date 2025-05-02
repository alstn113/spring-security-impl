package com.alstn113.security.security.authorization;

import com.alstn113.security.security.context.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class AuthorityAuthorizationManager implements AuthorizationManager {

    private final Set<String> authorities;

    public static AuthorityAuthorizationManager hasAuthority(String authority) {
        return new AuthorityAuthorizationManager(authority);
    }

    public AuthorityAuthorizationManager(String... authorities) {
        this.authorities = Set.of(authorities);
    }

    @Override
    public AuthorizationResult authorize(Supplier<Authentication> authentication, HttpServletRequest request) {
        boolean isGranted = isGranted(authentication.get(), authorities);

        return new AuthorityAuthorizationDecision(isGranted, createAuthorities(authorities));
    }

    private boolean isGranted(Authentication authentication, Collection<String> authorities) {
        return authentication != null && isAuthenticated(authentication, authorities);
    }

    private boolean isAuthenticated(Authentication authentication, Collection<String> authorities) {
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (authorities.contains(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    private Collection<GrantedAuthority> createAuthorities(Collection<String> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(authorities.size());
        for (String authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        return grantedAuthorities;
    }
}
