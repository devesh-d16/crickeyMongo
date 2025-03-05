package com.devesh.cricketmongo.dto;

import lombok.Data;

@Data
public class MatchRequestDTO {
    Long id;
    TeamRequestDTO team1;
    TeamRequestDTO team2;
    String venue;
    int overs;

}
