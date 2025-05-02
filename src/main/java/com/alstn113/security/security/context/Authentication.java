package com.alstn113.security.security.context;

import com.alstn113.security.security.authorization.GrantedAuthority;
import java.util.Collection;

public interface Authentication {

    Object principal();

    Collection<? extends GrantedAuthority> getAuthorities();
}
