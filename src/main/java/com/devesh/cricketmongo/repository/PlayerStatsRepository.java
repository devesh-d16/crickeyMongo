package com.devesh.cricketmongo.repository;

import com.devesh.cricketmongo.entity.PlayerStats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayerStatsRepository extends MongoRepository<PlayerStats, String> {
    Object findByPlayerId(Long playerId);
}
