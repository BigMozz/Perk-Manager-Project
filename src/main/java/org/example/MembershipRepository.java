package org.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    Membership findMembershipByMid(@Param("mid") Integer mid);

    List<Membership> findByUser(@Param("user") User user);

    List<Membership> findByUserUid(@Param("uid") Integer uid);
}