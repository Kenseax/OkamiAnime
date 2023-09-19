package ru.anime.okami.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.*;
import ru.anime.okami.form.UserDto;
import ru.anime.okami.model.Role;
import ru.anime.okami.model.User;
import ru.anime.okami.payload.RegisterDto;
import ru.anime.okami.repository.RoleRepository;
import ru.anime.okami.repository.UserRepository;
import ru.anime.okami.service.AuthService;
import ru.anime.okami.service.TokenService;

import java.io.IOException;
import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@CrossOrigin(origins = "https://423b-79-139-249-160.ngrok-free.app/", maxAge = 648000, allowCredentials = "true")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final TokenService tokenService;
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final RoleRepository roleRepository;

    private final HttpHeaders responseHeaders = new HttpHeaders();
    private final CsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();

    {
        responseHeaders.setAccessControlAllowMethods(List.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.GET, HttpMethod.DELETE, HttpMethod.OPTIONS));
        responseHeaders.setAccessControlAllowHeaders(List.of("Origin", "X-Api-Key", "X-Requested-With", "Content-Type", "Accept", "Authorization"));
    }

    record LoginRequest(String username, String password) {
    }

    record LoginResponse(String message, String access_jwt_token, String refresh_jwt_token) {
    }

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws ServletException, IOException {
        User user = userRepository.findByUsername(loginRequest.username).orElse(null);
        String access_token = tokenService.generateAccessToken(user);
        String refresh_token = tokenService.generateRefreshToken(user);

        request.login(loginRequest.username, loginRequest.password);

        return ResponseEntity.ok().headers(responseHeaders).body(new LoginResponse("User with email = " + loginRequest.username + " successfully logged in!"
                , access_token, refresh_token));
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto, HttpServletRequest request) {
        String response = authService.register(registerDto);
        return ResponseEntity.ok().headers(responseHeaders).body(response);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String authorizationToken = request.getHeader("Authorization");
        String name = tokenService.parseToken(authorizationToken);
        User user = userRepository.findByUsername(name).orElse(null);

        if (user == null) {
            return ResponseEntity.unprocessableEntity().build();
        }
        List<String> roles = roleRepository.findRolesByUserId(user.getId());

        UserDto userDto = new UserDto(user.getUsername(), user.getAvatar(), roles);

        return ResponseEntity.ok().headers(responseHeaders).body(userDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.logout();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return ResponseEntity.ok().headers(responseHeaders).body("Logged out");
    }

    record RefreshTokenResponse(String access_jwt_token, String refresh_jwt_token) {
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        String name = tokenService.parseToken(headerAuth);
        User user = userRepository.findByUsername(name).orElse(null);
        String access_token = tokenService.generateAccessToken(user);
        String refresh_token = tokenService.generateRefreshToken(user);

        return ResponseEntity.ok().headers(responseHeaders).body(new RefreshTokenResponse(access_token, refresh_token));
    }
}
