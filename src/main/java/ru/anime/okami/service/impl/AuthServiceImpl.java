package ru.anime.okami.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.anime.okami.exception.AnimeAPIException;
import ru.anime.okami.model.Role;
import ru.anime.okami.repository.RoleRepository;
import ru.anime.okami.model.User;
import ru.anime.okami.repository.UserRepository;
import ru.anime.okami.payload.LoginDto;
import ru.anime.okami.payload.RegisterDto;
import ru.anime.okami.service.AuthService;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication.getName();
    }

    @Override
    public String register(RegisterDto registerDto) {

        // add check for username exists in database
        if(Boolean.TRUE.equals(userRepository.existsByUsername(registerDto.getUsername()))){
            throw new AnimeAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        // add check for email exists in database
        if(Boolean.TRUE.equals(userRepository.existsByMail(registerDto.getMail()))){
            throw new AnimeAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        User user = new User();
        //user.setFirstName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setMail(registerDto.getMail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully and automatically logged in!";
    }
}
