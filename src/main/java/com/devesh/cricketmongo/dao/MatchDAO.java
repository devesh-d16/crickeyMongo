package com.devesh.cricketmongo.dao;

import com.devesh.cricketmongo.dto.InningsDTO;
import com.devesh.cricketmongo.entity.*;
import com.devesh.cricketmongo.enums.MatchStatus;
import com.devesh.cricketmongo.exceptions.GameRuleException;
import com.devesh.cricketmongo.exceptions.ResourceNotFoundException;
import com.devesh.cricketmongo.repository.*;
import com.devesh.cricketmongo.service.InningService;
import com.devesh.cricketmongo.service.MatchService;
import com.devesh.cricketmongo.service.TeamService;
import com.devesh.cricketmongo.service.TeamStatService;
import com.devesh.cricketmongo.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchDAO {

    private final MatchService matchService;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final TeamService teamService;
    private final TeamStatService teamStatService;
    private final Mapper mapper;
    private final InningService inningService;
    private final TeamStatsRepository teamStatsRepository;
    private final PlayerStatsRepository playerStatsRepository;


    public List<Match> getAllMatches() {
        List<Match> matches = matchRepository.findAll();
        if(matches.isEmpty()){
            return Collections.emptyList();
        }
        return matches;
    }

    public Match getMatchById(Long matchId) {
       return matchRepository.findMatchById((matchId)).orElseThrow(() -> new ResourceNotFoundException("Match with id " + matchId + " not found"));
    }

    public Team getWinnerByMatchId(Long id){
        Match match = matchRepository.findMatchById(id).orElseThrow(() -> new ResourceNotFoundException("Match with id " + id + " not found"));
        TeamStats winner = teamStatService.findByTeamStatId(match.getWinnerId());
        Team winnerTeam = teamService.getTeamById(winner.getTeamId());
        if(match.getWinnerId() == null){
            throw new GameRuleException("Match is drawn");
        }
        return winnerTeam;
    }

    public List<Match> findAllMatchWonByTeam(String teamId) {
        Team team = teamRepository.findById(teamId).orElse(null);
        if(team == null) {
            return Collections.emptyList();
        }
        return matchRepository.getMatchByWinnerId(teamId);
    }

    public List<Match> getMatchesByMatchStatus(String matchStatus) {
        try {
            MatchStatus status = MatchStatus.valueOf(matchStatus.toUpperCase());
            return matchRepository.getMatchByMatchStatus(status);
        } catch (GameRuleException e) {
            throw new GameRuleException("Match status " + matchStatus + " not supported");
        }
    }

    public List<InningsDTO> getMatchScoreboard(Long id) {
        Match match = getMatchById(id);
        List<InningsDTO> inningsDTOs = new ArrayList<>();
        Inning inning1 = inningService.getInningById(match.getInningIds().getFirst());
        Inning inning2 = inningService.getInningById(match.getInningIds().getLast());
        inningsDTOs.add(mapper.convertToInningsDTO(inning1));
        inningsDTOs.add(mapper.convertToInningsDTO(inning2));
        return inningsDTOs;
    }

    public List<Match> getMatchByTeamPlayedName(String team1Name, String team2Name) {
        Team team1 = teamService.getTeamByName(team1Name);
        Team team2 = teamService.getTeamByName(team2Name);

        return getAllMatches().stream().filter(match -> (teamStatService.findByTeamStatId(match.getTeam1Id()).getTeamId().equals(team1.getId()) && teamStatService.findByTeamStatId(match.getTeam2Id()).getTeamId().equals(team2.getId())) ||
                (teamStatService.findByTeamStatId(match.getTeam1Id()).getTeamId().equals(team2.getId()) && teamStatService.findByTeamStatId(match.getTeam2Id()).getTeamId().equals(team1.getId()))).collect(Collectors.toList());
    }

    public List<Match> getMatchByTeamName(String name) {
        Team team = teamService.getTeamByName(name);
        return getAllMatches().stream()
                .filter(match -> teamStatService.findByTeamStatId(match.getTeam1Id()).getTeamId().equals(team.getId()) || teamStatService.findByTeamStatId(match.getTeam2Id()).getTeamId().equals(team.getId()))
                .collect(Collectors.toList());
    }
//
    @Transactional
    public String deleteMatchById(Long id) {
        Match match = getMatchById(id);
        if(match == null){
            return "No match with match id " + id + " found";
        }
        try{
            List<String> innings = match.getInningIds();
            inningService.deleteAllInningsById(innings);
            TeamStats team1 = teamStatsRepository.getTeamStatsById(match.getTeam1Id());
            List<PlayerStats> players1 = team1.getPlayerIds().stream().map(
                    playerId -> playerStatsRepository.findById(playerId).orElseThrow(() -> new RuntimeException())
            ).collect(Collectors.toList());
            teamStatsRepository.delete(team1);
            playerStatsRepository.deleteAll(players1);

            TeamStats team2 = teamStatsRepository.getTeamStatsById(match.getTeam2Id());
            List<PlayerStats> players2 = team2.getPlayerIds().stream().map(
                    playerId -> playerStatsRepository.findById(playerId).orElseThrow(() -> new RuntimeException())
            ).collect(Collectors.toList());
            teamStatsRepository.delete(team2);
            playerStatsRepository.deleteAll(players2);

            matchRepository.deleteById(id);
            return "Match deleted successfully";
        }
        catch (Exception e){
            throw new RuntimeException();
        }
    }
//
//    public Match updateMatch(Long id, MatchUpdateDTO matchUpdateDTO) {
//        Match match = getMatchById(id);
//        match.setLastUpdate(LocalDateTime.now());
//        match.setVenue(matchUpdateDTO.getVenue());
//        matchSqlRepository.save(match);
//        return match;
//    }
//
    public List<Match> getMatchesByVenue(String venue) {
        return matchRepository.getMatchByVenue(venue);
    }

    public List<Match> getMatchByWinnerTeamName(String name) {
        Team team = teamService.getTeamByName(name);
        return getAllMatches().stream().filter(match -> teamStatService.findByTeamStatId(match.getWinnerId()).getTeamId().equals(team.getId())).collect(Collectors.toList());
    }
}
