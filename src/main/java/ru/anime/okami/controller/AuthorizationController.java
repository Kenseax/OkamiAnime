package ru.anime.okami.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;
import ru.anime.okami.payload.LoginDto;
import ru.anime.okami.payload.RegisterDto;
import ru.anime.okami.service.AuthService;
import ru.anime.okami.utils.HeaderConst;
import ru.anime.okami.utils.HeaderConst.*;


@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 648000, allowCredentials = "true")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {


    private final AuthenticationProvider authenticationProvider;

    private final AuthService authService;
    private final HttpHeaders responseHeaders = new HttpHeaders();

    {
       // responseHeaders.add(HeaderConst.ACCESS_CONTROL_ALLOW_ORIGIN, HeaderConst.ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        responseHeaders.add(HeaderConst.ACCESS_CONTROL_ALLOW_METHODS, HeaderConst.ACCESS_CONTROL_ALLOW_METHODS_VALUE);
        responseHeaders.add(HeaderConst.ACCESS_CONTROL_ALLOW_HEADERS, HeaderConst.ACCESS_CONTROL_ALLOW_HEADERS_VALUE);
        responseHeaders.add(HeaderConst.ACCESS_CONTROL_EXPOSE_HEADERS, HeaderConst.ACCESS_CONTROL_EXPOSE_HEADERS_VALUE);
       // responseHeaders.add(HeaderConst.ACCESS_CONTROL_ALLOW_CREDENTIALS, HeaderConst.ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUE);
        responseHeaders.add(HeaderConst.CACHE_CONTROL, HeaderConst.CACHE_CONTROL_VALUE);
    }

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletRequest request) throws ServletException {
        //String login = authService.login(loginDto);
        request.login(loginDto.getUsername(), loginDto.getPassword());
        HttpSession session = request.getSession();
        session.setAttribute("username", loginDto.getUsername());
        return ResponseEntity.ok().headers(responseHeaders).body(loginDto.getUsername());
    }

    // Build Register REST API
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto, HttpServletRequest request) {

        String response = authService.register(registerDto);
        //doAutoLogin(registerDto.getUsername(), registerDto.getPassword(), request);
        return ResponseEntity.ok().headers(responseHeaders).body(response);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) session.getAttribute("username");

        return ResponseEntity.ok().headers(responseHeaders).body(username);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return ResponseEntity.ok("Logged out successfully!");
    }

    private void doAutoLogin(String username, String password, HttpServletRequest request) {

        try {
            // Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authentication = this.authenticationProvider.authenticate(token);
            log.debug("Logging in with [{}]", authentication.getPrincipal());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            log.error("Failure in autoLogin", e);
        }

    }
}
