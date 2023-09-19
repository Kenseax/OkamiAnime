package ru.anime.okami.form;

import org.springframework.security.core.GrantedAuthority;
import ru.anime.okami.model.Role;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public record UserDto(String username, String avatar, List<String> authorities) {
}
