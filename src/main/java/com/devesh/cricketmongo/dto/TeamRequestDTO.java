package com.devesh.cricketmongo.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamRequestDTO {

    String teamName;
    List<String> players;
}
