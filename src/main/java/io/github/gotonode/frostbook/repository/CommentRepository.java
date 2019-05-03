package io.github.gotonode.frostbook.repository;

import io.github.gotonode.frostbook.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Override
    @Query
     List<Comment> findAll();

}
