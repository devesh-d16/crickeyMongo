package com.devesh.cricketmongo.repository;

import com.devesh.cricketmongo.entity.Player;
import com.devesh.cricketmongo.enums.PlayerRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

    boolean existsByName(String name);

    Optional<Player> findByName(String name);

    List<Player> findAllByRole(PlayerRole role);

    boolean existsPlayerByName(String name);

    Player getPlayerByName(String name);

    Optional<Player> getPlayerById(Long playerId);

    List<Player> getAllByRole(PlayerRole role);

    void deleteById(Long id);
}
