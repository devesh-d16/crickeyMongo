package com.devesh.cricketmongo.repository;

import com.devesh.cricketmongo.entity.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TeamRepository extends MongoRepository<Team, String> {
    boolean existsByName(String name);

    Optional<Team> findByName(String name);

    Optional<Team> getTeamById(Long id);
}
