package ru.anime.okami.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anime.okami.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
