package ru.anime.okami.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.anime.okami.model.Role;
import ru.anime.okami.repository.RoleRepository;
import ru.anime.okami.model.User;
import ru.anime.okami.repository.UserRepository;

import java.util.List;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public CustomAuthProvider(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal() + "";
        String password = authentication.getCredentials() + "";

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new BadCredentialsException("Username not found");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        List<Role> userRights = roleRepository.findByName("ROLE_USER").stream().toList();
        return new UsernamePasswordAuthenticationToken(username, password, userRights.stream().map(x -> new SimpleGrantedAuthority(x.getName())).toList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
