package ru.anime.okami.form;

import java.util.List;

public record UserDto(String username, String avatar, List<String> authorities) {
}
