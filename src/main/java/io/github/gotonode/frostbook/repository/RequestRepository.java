package io.github.gotonode.frostbook.repository;

import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Request findByFromProfile(Profile fromProfile);

    List<Request> findAllByFromProfile(Profile fromProfile);

}
