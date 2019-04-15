package io.github.gotonode.frostbook.repository;

import io.github.gotonode.frostbook.domain.Subcomment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcommentRepository extends JpaRepository<Subcomment, Long> {

}
