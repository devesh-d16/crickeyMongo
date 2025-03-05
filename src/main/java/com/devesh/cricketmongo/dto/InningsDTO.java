package com.devesh.cricketmongo.dto;

import lombok.Data;

@Data
public class InningsDTO {

    private int runs;
    private int wickets;
    private int overs;
    private String battingTeam;
    private String bowlingTeam;
    private ScoreboardDTO scoreboardDTO;
}
