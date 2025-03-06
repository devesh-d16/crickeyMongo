package com.devesh.cricketmongo.repository;

import com.devesh.cricketmongo.entity.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TeamRepository extends MongoRepository<Team, String> {
    boolean existsByTeamName(String teamName);

    Optional<Team> findByTeamName(String teamName);

    Optional<Team> findTeamByTeamId(Long teamId);
}
