package org.example;

import org.example.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserByEmail(@Param("email") String email);
    User findUserById(@Param("uid") String uid);

}
