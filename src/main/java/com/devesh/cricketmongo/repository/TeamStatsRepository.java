package com.devesh.cricketmongo.repository;

import com.devesh.cricketmongo.entity.TeamStats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamStatsRepository extends MongoRepository<TeamStats, String> {
    TeamStats getTeamStatsById(String team1Id);
}
