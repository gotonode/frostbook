package io.github.gotonode.frostbook;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findProfileById(long id);

    Profile findProfileByHandle(String handle);

    Profile findProfileByName(String name);

    Profile findProfileByPath(String path);

    /**
     * This method has a very long name but it works.
     * @param handle What we are searching for.
     * @param name What we are searching for.
     * @param path What we are searching for.
     * @return A list of Profile-objects for your convenience.
     */
    List<Profile> findAllByHandleContainingOrNameContainingOrPathContainingOrderByIdAsc
    (String handle, String name, String path);

    List<Profile> findAllByOrderByIdAsc();

}
