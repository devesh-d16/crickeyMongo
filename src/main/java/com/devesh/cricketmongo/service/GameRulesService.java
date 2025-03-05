package com.devesh.cricketmongo.service;

import com.devesh.cricketmongo.entity.Inning;
import com.devesh.cricketmongo.entity.PlayerStats;
import com.devesh.cricketmongo.entity.TeamStats;
import com.devesh.cricketmongo.model.StrikePair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameRulesService {

    public boolean gameEnd(TeamStats batting, Inning inning, int targetRun) {
        return (inning.getWickets() >= 10 || (targetRun != -1 && batting.getRuns() > targetRun));
    }

    public void swapStrikers(StrikePair strikePair) {
        PlayerStats temp = strikePair.playerOnStrike;
        strikePair.playerOnStrike = strikePair.playerOffStrike;
        strikePair.playerOffStrike = temp;
    }

    public void rotateStrike(int run, StrikePair strikePair) {
        if (run % 2 != 0) {
            swapStrikers(strikePair);
        }
    }
}