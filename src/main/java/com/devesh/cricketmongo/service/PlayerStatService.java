package com.devesh.cricketmongo.service;


import com.devesh.cricketmongo.entity.Player;
import com.devesh.cricketmongo.entity.PlayerStats;
import com.devesh.cricketmongo.entity.TeamStats;
import com.devesh.cricketmongo.repository.PlayerStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerStatService {

    private final PlayerStatsRepository playerStatsRepository;

//    public PlayerStats createPlayerStats(Player player, TeamStats teamStats) {
//        PlayerStats playerStats = new PlayerStats();
//        playerStats.setPlayer(player);
//        playerStats.setTeamStats(teamStats);
//        return playerStatsRepository.save(playerStats);
//    }

    public PlayerStats findById(String id){
        return playerStatsRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
