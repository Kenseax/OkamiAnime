package ru.anime.okami.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import ru.anime.okami.model.User;
import ru.anime.okami.payload.RegisterDto;
import ru.anime.okami.service.AuthService;
import ru.anime.okami.service.TokenService;
import ru.anime.okami.service.impl.UserServiceImpl;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@CrossOrigin(origins = "https://262d-79-139-249-160.ngrok-free.app/", maxAge = 648000, allowCredentials = "true")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final TokenService tokenService;
    private final AuthenticationManager authManager;
    private final UserServiceImpl userService;
    private final AuthService authService;

    private final HttpHeaders responseHeaders = new HttpHeaders();

    {
        responseHeaders.setAccessControlAllowMethods(List.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.GET, HttpMethod.DELETE, HttpMethod.OPTIONS));
        responseHeaders.setAccessControlAllowHeaders(List.of("Origin", "X-Api-Key", "X-Requested-With", "Content-Type", "Accept", "Authorization"));
        responseHeaders.setAccessControlExposeHeaders(List.of("Set-Cookie"));
        responseHeaders.setCacheControl("max-age=648000");
    }

    record LoginRequest(String username, String password) {
    }

    record LoginResponse(String message, String access_jwt_token, String refresh_jwt_token) {
    }

    @PostMapping(value = {"/login", "/signin"})
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws ServletException, IOException {
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password);
//        Authentication auth = authManager.authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = (User) userService.loadUserByUsername(loginRequest.username);
        String access_token = tokenService.generateAccessToken(user);
        String refresh_token = tokenService.generateRefreshToken(user);

        request.login(loginRequest.username, loginRequest.password);

        return new LoginResponse("User with email = " + loginRequest.username + " successfully logged in!"
                , access_token, refresh_token);
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto, HttpServletRequest request) {

        String response = authService.register(registerDto);
        //doAutoLogin(registerDto.getUsername(), registerDto.getPassword(), request);
        return ResponseEntity.ok().headers(responseHeaders).body(response);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String authorizationToken = request.getHeader("Authorization");
        String s = tokenService.parseToken(authorizationToken);
        User user = (User) userService.loadUserByUsername(s);
        //refreshToken(request);

        return ResponseEntity.ok().headers(responseHeaders).body(user);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return ResponseEntity.ok().headers(responseHeaders).body("Logged out");
    }

    record RefreshTokenResponse(String access_jwt_token, String refresh_jwt_token) {
    }

    @CrossOrigin(origins = "https://262d-79-139-249-160.ngrok-free.app/")
    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        //String refreshToken = headerAuth.substring(7, headerAuth.length());
        String name = tokenService.parseToken(headerAuth);
        User user = (User) userService.loadUserByUsername(name);
        String access_token = tokenService.generateAccessToken(user);
        String refresh_token = tokenService.generateRefreshToken(user);

        return ResponseEntity.ok().headers(responseHeaders).body(new RefreshTokenResponse(access_token, refresh_token));
    }
}
