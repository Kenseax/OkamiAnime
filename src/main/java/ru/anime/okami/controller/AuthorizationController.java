package ru.anime.okami.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import ru.anime.okami.form.UserDto;
import ru.anime.okami.model.User;
import ru.anime.okami.payload.RegisterDto;
import ru.anime.okami.repository.RoleRepository;
import ru.anime.okami.repository.UserRepository;
import ru.anime.okami.service.AuthService;
import ru.anime.okami.service.TokenService;
import ru.anime.okami.service.UserService;
import ru.anime.okami.service.impl.DataService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final TokenService tokenService;
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final DataService dataService;

    private final HttpHeaders responseHeaders = new HttpHeaders();

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

        HttpSession session = request.getSession();
        session.invalidate();

        request.login(loginRequest.username, loginRequest.password);

        return ResponseEntity.ok().headers(responseHeaders).body(generateToken(loginRequest.username));
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterDto registerDto, HttpServletRequest request) {
        String regName = authService.register(registerDto);
        return ResponseEntity.ok().headers(responseHeaders).body(generateToken(regName));
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) throws IOException, URISyntaxException {
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //request.logout();
        HttpSession session = request.getSession();
        session.invalidate();
        return ResponseEntity.ok().headers(responseHeaders).header("Cookie", "").body("Logged out");
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

    public LoginResponse generateToken(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return new LoginResponse("User not found", "", "");
        }
        String access_token = tokenService.generateAccessToken(user);
        String refresh_token = tokenService.generateRefreshToken(user);
        return new LoginResponse("User: " + username + " successfully singed in", access_token, refresh_token);
    }
}
