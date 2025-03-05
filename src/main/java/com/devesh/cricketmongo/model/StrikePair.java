package com.devesh.cricketmongo.model;

import com.devesh.cricketmongo.entity.PlayerStats;
import lombok.Getter;

@Getter
public class StrikePair {
    public PlayerStats playerOnStrike;
    public PlayerStats playerOffStrike;
    private int nextBat = 2;


    public void nextBatsman() {
        this.nextBat++;
    }

    public StrikePair(PlayerStats playerOnStrike, PlayerStats playerOffStrike) {
        this.playerOnStrike = playerOnStrike;
        this.playerOffStrike = playerOffStrike;
    }
}
