package io.github.gotonode.frostbook.repository;

import io.github.gotonode.frostbook.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
