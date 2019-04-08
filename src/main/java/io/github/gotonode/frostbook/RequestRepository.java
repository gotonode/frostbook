package io.github.gotonode.frostbook;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Request findByFromProfile(Profile fromProfile);
}
