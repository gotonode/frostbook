package io.github.gotonode.frostbook.repository;

import io.github.gotonode.frostbook.domain.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @EntityGraph(attributePaths = {"images", "comments", "authorities"})
    @Override
    List<Profile> findAll();

    @EntityGraph(attributePaths = {"images", "comments", "authorities", "likedBy"})
    @Override
    Optional<Profile> findById(Long id);

    Profile findByHandle(String handle);

    Profile findByName(String name);

    Profile findByPath(String path);

    /**
     * This method has a very long name but it works.
     *
     * @param handle What we are searching for.
     * @param name   What we are searching for.
     * @param path   What we are searching for.
     * @return A list of Profile-objects for your convenience.
     */

    List<Profile> findAllByHandleContainingOrNameContainingOrPathContainingOrderByIdAsc
    (String handle, String name, String path);

    List<Profile> findAllByOrderByIdAsc();

}
