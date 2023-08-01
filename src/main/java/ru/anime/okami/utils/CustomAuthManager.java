package ru.anime.okami.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthManager implements AuthenticationManager {

    private final CustomAuthProvider provider;

    @Autowired
    public CustomAuthManager(CustomAuthProvider provider) {
        this.provider = provider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SecurityContextHolder.getContext().setAuthentication(provider.authenticate(authentication));
        return provider.authenticate(authentication);
    }
}
