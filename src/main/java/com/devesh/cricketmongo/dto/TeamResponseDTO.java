package com.devesh.cricketmongo.dto;

import lombok.Data;

@Data
public class TeamResponseDTO {
    private String teamName;
    private int runs;
    private int wickets;
    private int overs;
}
