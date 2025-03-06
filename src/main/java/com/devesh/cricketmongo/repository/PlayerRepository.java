package com.devesh.cricketmongo.repository;

import com.devesh.cricketmongo.entity.Player;
import com.devesh.cricketmongo.enums.PlayerRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

    boolean existsByPlayerName(String playerName);

    Optional<Player> findByPlayerName(String playerName);

    List<Player> findAllByPlayerRole(PlayerRole playerRole);

    Optional<Player> findByPlayerId(Long playerId);

    void deleteByPlayerId(Long playerId);
}
