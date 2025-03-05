package com.devesh.cricketmongo.service;

import com.devesh.cricketmongo.entity.TeamStats;
import com.devesh.cricketmongo.repository.TeamStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamStatService {

    private final TeamStatsRepository teamStatsRepository;

    public TeamStats findByTeamStatId(String id){
        return teamStatsRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}

