package io.github.gotonode.frostbook;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findProfileById(long id);

    Profile findProfileByHandle(String handle);

    Profile findProfileByName(String name);

    Profile findProfileByPath(String path);

    List<Profile> findAllByHandleOrNameOrPathOrderByIdAsc(String handle, String name, String path);

    List<Profile> findAllByOrderByIdAsc();

}
