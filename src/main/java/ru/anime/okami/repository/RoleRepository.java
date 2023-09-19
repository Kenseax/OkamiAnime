package ru.anime.okami.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.anime.okami.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);

    @Query("SELECT R.name FROM Role R " +
            "INNER JOIN R.users UR " +
            "WHERE UR.id = :userId")
    List<String> findRolesByUserId(Long userId);
}
