package org.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Rating, Integer> {
    Rating findByRid(Integer rid);

    List<Rating> findByPerk(Perk perk);

    List<Rating> findByUser(User user);
}
