package io.github.gotonode.frostbook.repository;

import io.github.gotonode.frostbook.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
