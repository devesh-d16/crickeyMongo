package com.devesh.cricketmongo.model;

import com.devesh.cricketmongo.entity.TeamStats;
import lombok.Data;

@Data
public class Result {
    private TeamStats winner;
    private int winningMargin;
    private String winningCondition;
}
