package ru.anime.okami.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.anime.okami.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    void addUser(User user) throws Exception;

    User updateUser(User user, long id) throws Exception;

    void deleteUser(long id);

    User getUserById(long id);

    List<User> getUsers();

}
