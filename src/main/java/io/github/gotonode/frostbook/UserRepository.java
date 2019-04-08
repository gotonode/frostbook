package io.github.gotonode.frostbook;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(long id);

    User findUserByHandle(String handle);

    User findUserByName(String name);

    User findUserByPath(String path);

    List<User> findAllByHandleOrNameOrPathOrderByIdAsc(String handle, String name, String path);

    List<User> findAllByOrderByIdAsc();

}
