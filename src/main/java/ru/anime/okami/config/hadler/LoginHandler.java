package ru.anime.okami.config.hadler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.anime.okami.model.User;
import ru.anime.okami.repository.UserRepository;

import java.io.IOException;
import java.util.Set;

@Component
public class LoginHandler implements AuthenticationSuccessHandler {

    private UserRepository userRepository;

    @Autowired
    public LoginHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginHandler() {
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        if (roles.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/admin/");
        } else if (roles.contains("ROLE_USER")) {
            httpServletResponse.sendRedirect("/current");
        }
    }
}
