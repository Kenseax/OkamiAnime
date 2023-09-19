package ru.anime.okami.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.anime.okami.model.Role;
import ru.anime.okami.model.User;

import java.util.Optional;
import java.util.Set;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMail(String email);

    Optional<User> findByUsernameOrMail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByMail(String email);
}
