package ru.anime.okami.form;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class LoginForm {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
