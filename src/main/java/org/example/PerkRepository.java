package org.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerkRepository extends JpaRepository<Perk, Integer> {
    Perk findPerkByPid(@Param("pid") Integer pid);
}