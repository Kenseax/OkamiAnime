package ru.anime.okami.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.anime.okami.exception.UserAlreadyExistException;
import ru.anime.okami.model.User;
import ru.anime.okami.repository.UserRepository;
import ru.anime.okami.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void addUser(User user) throws Exception {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("There is an account with that nickname: "
                    + user.getUsername());
        }

        if (userRepository.findByMail(user.getMail()).isPresent()) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + user.getMail());
        }

        checkCredentials(user.getUsername(), user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user, long id) throws Exception {
        if (userRepository.findByUsername(user.getUsername()).isPresent() && !userRepository.findById(id).get().getUsername().equals(user.getUsername())) {
            throw new UserAlreadyExistException("There is an account with that nickname: "
                    + user.getUsername());
        }

        if (userRepository.findByMail(user.getMail()).isPresent() && !userRepository.findById(id).get().getMail().equals(user.getMail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + user.getMail());
        }

        checkCredentials(user.getUsername(), user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return userRepository.findByUsername(name).orElse(null);
    }

    public void checkCredentials(String login, String password) throws Exception {
        if (login.length() < 3 || login.length() > 20) {
            throw new Exception("Логин должен содержать от 3 до 20 символов");
        }

        if (password.length() < 8 || password.length() > 25) {
            throw new Exception("Пароль должен содержать минимум 8 символов");
        }
    }
}
