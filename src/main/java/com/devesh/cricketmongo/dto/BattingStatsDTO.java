package com.devesh.cricketmongo.dto;

import lombok.Data;

@Data
public class BattingStatsDTO {
    private String name;
    private int runsScored;
    private int ballsFaced;
}
