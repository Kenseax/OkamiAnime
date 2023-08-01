package ru.anime.okami.service;


import org.springframework.stereotype.Service;
import ru.anime.okami.payload.LoginDto;
import ru.anime.okami.payload.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
