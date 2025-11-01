package org.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerkRepository extends CrudRepository<Perk, Integer> {
        Perk findPerkById(@Param("pid") String pid);
}