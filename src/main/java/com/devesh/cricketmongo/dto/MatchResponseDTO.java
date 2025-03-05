package com.devesh.cricketmongo.dto;

import lombok.Data;

@Data
public class MatchResponseDTO {
    private String title;
    private String venue;
    private int overs;
    private String result;
    TeamResponseDTO team1;
    TeamResponseDTO team2;
}
