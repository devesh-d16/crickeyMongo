package com.devesh.cricketmongo.utils;

import com.devesh.cricketmongo.dto.*;
import com.devesh.cricketmongo.entity.*;
import com.devesh.cricketmongo.enums.PlayerRole;
import com.devesh.cricketmongo.repository.TeamStatsRepository;
import com.devesh.cricketmongo.service.PlayerService;
import com.devesh.cricketmongo.service.PlayerStatService;
import com.devesh.cricketmongo.service.TeamService;
import com.devesh.cricketmongo.service.TeamStatService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@Component
public class Mapper {

    private final TeamStatsRepository teamStatsRepository;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final TeamStatService teamStatService;
    private final PlayerStatService playerStatService;

    public List<MatchResponseDTO> convertToList(List<Match> matches){
        return matches.stream().map(this::getMatchResponse).collect(Collectors.toList());
    }

    public MatchResponseDTO getMatchResponse(Match match) {

        MatchResponseDTO dto = new MatchResponseDTO();

        TeamStats teamStats1 = teamStatService.findByTeamStatId(match.getTeam1Id());
        Team team1 = teamService.getTeamById(teamStats1.getTeamId());

        TeamStats teamStats2 = teamStatService.findByTeamStatId(match.getTeam2Id());
        Team team2 = teamService.getTeamById(teamStats2.getTeamId());

        dto.setTitle(team1.getTeamName() + " v/s " + team2.getTeamName());
        dto.setVenue(match.getVenue());
        dto.setOvers(match.getOvers());
        dto.setResult(match.getWinningCondition());
        dto.setTeam1(convertToTeamDTO(match.getTeam1Id()));
        dto.setTeam2(convertToTeamDTO(match.getTeam2Id()));

        return dto;
    }

    public TeamResponseDTO convertToTeamDTO(String teamId) {
        if (teamId == null) return null;// Avoid NullPointerException
        TeamStats team = teamStatsRepository.findById(teamId).orElseThrow(RuntimeException::new);
        TeamResponseDTO dto = new TeamResponseDTO();
        dto.setTeamName(teamService.getTeamById(team.getTeamId()).getTeamName());
        dto.setRuns(team.getRuns());
        dto.setWickets(team.getWickets());
        dto.setOvers(team.getOvers());
        return dto;
    }

    public InningsDTO convertToInningsDTO(Inning innings){
        InningsDTO inningsDTO = new InningsDTO();
        Team batting = teamService.getTeamById(teamStatService.findByTeamStatId(innings.getBattingTeamId()).getTeamId());
        Team bowling = teamService.getTeamById(teamStatService.findByTeamStatId(innings.getBowlingTeamId()).getTeamId());

        inningsDTO.setBattingTeam(batting.getTeamName());
        inningsDTO.setBowlingTeam(bowling.getTeamName());
        inningsDTO.setRuns(innings.getRuns());
        inningsDTO.setWickets(innings.getWickets());
        inningsDTO.setOvers(innings.getOvers());
        inningsDTO.setScoreboardDTO(convertToScoreboardDTO(innings));
        return inningsDTO;
    }

    public ScoreboardDTO convertToScoreboardDTO(Inning innings) {
        ScoreboardDTO scoreboardDTO = new ScoreboardDTO();
        TeamStats battingTeam = teamStatService.findByTeamStatId(innings.getBattingTeamId());
        TeamStats bowlingTeam = teamStatService.findByTeamStatId(innings.getBowlingTeamId());
        List<BattingStatsDTO> batterStats = convertToBattingStatsDTO(battingTeam);
        List<BowlingStatsDTO> bowlerStats = convertToBowlingStatsDTO(bowlingTeam);
        scoreboardDTO.setBattersStats(batterStats);
        scoreboardDTO.setBowlerStats(bowlerStats);
        return scoreboardDTO;
    }

    public List<BowlingStatsDTO> convertToBowlingStatsDTO(TeamStats bowlingTeam) {
        List<BowlingStatsDTO> bowl =  new ArrayList<>();
        List<PlayerStats> bowlers = bowlingTeam.getBowlerIds().stream().map(
                playerStatService::findById
        ).collect(Collectors.toList());


        for(PlayerStats bowler : bowlers){
            Player player = playerService.getPlayerById(bowler.getPlayerId());
            if(player.getPlayerRole()== PlayerRole.BOWLER) {
                BowlingStatsDTO bowlingStatsDTO = new BowlingStatsDTO();
                bowlingStatsDTO.setName(player.getPlayerName());
                bowlingStatsDTO.setOversBowled((bowler.getBallsBowled())/6);
                bowlingStatsDTO.setRunsConceded(bowler.getRunsConceded());
                bowlingStatsDTO.setWicketsTaken(bowler.getWicketsTaken());
                bowl.add(bowlingStatsDTO);
            }
        }
        return bowl;
    }

    public List<BattingStatsDTO> convertToBattingStatsDTO(TeamStats battingTeam) {
        List<PlayerStats> players = battingTeam.getPlayerIds().stream().map(
                playerStatService::findById
        ).collect(Collectors.toList());

        List<BattingStatsDTO> bat = new ArrayList<>();

        for(PlayerStats batter : players){
            Player player = playerService.getPlayerById(batter.getPlayerId());
            BattingStatsDTO battingStatsDTO = new BattingStatsDTO();
            battingStatsDTO.setName(player.getPlayerName());
            battingStatsDTO.setRunsScored(batter.getRunsScored());
            battingStatsDTO.setBallsFaced(batter.getBallsFaced());
            bat.add(battingStatsDTO);
        }
        return bat;
    }

}
