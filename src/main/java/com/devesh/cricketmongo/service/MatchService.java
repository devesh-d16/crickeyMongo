package com.devesh.cricketmongo.service;

import com.devesh.cricketmongo.dto.*;
import com.devesh.cricketmongo.entity.*;
import com.devesh.cricketmongo.enums.MatchStatus;
import com.devesh.cricketmongo.enums.PlayerRole;
import com.devesh.cricketmongo.model.Result;
import com.devesh.cricketmongo.repository.MatchRepository;
import com.devesh.cricketmongo.repository.PlayerStatsRepository;
import com.devesh.cricketmongo.repository.TeamStatsRepository;
import com.devesh.cricketmongo.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final TeamService teamService;
    private final PlayerService playerService;
    private final InningService inningService;
    private final ResultService resultService;
    private final MatchRepository matchRepository;
    private final TeamStatsRepository teamStatsRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final Mapper mapper;

    public MatchResponseDTO startMatch(MatchRequestDTO matchRequestDTO) {

        // validate the team information
        validateMatchRequest(matchRequestDTO);

        TeamStats team1 = initializeTeamMatchStats(matchRequestDTO.getTeam1());
        TeamStats team2 = initializeTeamMatchStats(matchRequestDTO.getTeam2());
        Long id = matchRequestDTO.getId();
        int overs = matchRequestDTO.getOvers();

        // creating match object
        Match match = createMatch(id, team1, team2, overs, matchRequestDTO.getVenue());
        match.setMatchStatus(MatchStatus.IN_PROGRESS);

        // list to store both innings
        List<String> inningList = new ArrayList<>();

        Inning firstInnings = simulateInning(match, team1, team2, -1, overs);  // (targetRuns for first innings = -1)
        inningList.add(firstInnings.getInningId());

        Inning secondInnings = simulateInning(match, team2, team1, firstInnings.getRuns(), overs);
        inningList.add(secondInnings.getInningId());

        teamStatsRepository.saveAll(List.of(team1, team2));
        match.setInningIds(inningList);

        // To handle match result
        matchResult(match, firstInnings, secondInnings);
        matchRepository.save(match);

        return mapper.getMatchResponse(match);
    }


    private void validateMatchRequest(MatchRequestDTO matchRequestDTO) {
        String team1Name = matchRequestDTO.getTeam1().getTeamName();
        String team2Name = matchRequestDTO.getTeam2().getTeamName();

        // Teams are not the same
        if (team1Name.equalsIgnoreCase(team2Name)) {
            throw new IllegalArgumentException("Both teams cannot be the same.");
        }

        // Not have more than 11 players
        if (matchRequestDTO.getTeam1().getPlayers().size() > 11) {
            throw new IllegalArgumentException("Team " + team1Name + " has more than 11 players.");
        }
        if (matchRequestDTO.getTeam2().getPlayers().size() > 11) {
            throw new IllegalArgumentException("Team " + team2Name + " has more than 11 players.");
        }

        // No common players
        Set<String> team1Players = new HashSet<>(matchRequestDTO.getTeam1().getPlayers());
        Set<String> team2Players = new HashSet<>(matchRequestDTO.getTeam2().getPlayers());

        team1Players.retainAll(team2Players); // Finds common players

        if (!team1Players.isEmpty()) {
            throw new IllegalArgumentException("Teams cannot have common players: " + team1Players);
        }
    }


    public TeamStats initializeTeamMatchStats(TeamRequestDTO teamRequestDTO) {
        Team team = teamService.getTeamByName(teamRequestDTO.getTeamName());
        List<Player> players = teamRequestDTO.getPlayers().stream().map(
                (player) -> playerService.getPlayerByName(player)).collect(Collectors.toList());

        TeamStats teamStats = new TeamStats();

        teamStats.setTeamId(team.getTeamId());

        List<String> bowlers = new ArrayList<>();
        List<String> matchPlayers = players.stream()
                .map(player -> {
                            PlayerStats matchPlayer = new PlayerStats();
                            matchPlayer.setPlayerId(player.getPlayerId());
                            matchPlayer.setTeamId(teamStats.getTeamStatsId());
                            playerStatsRepository.save(matchPlayer);
                            if(player.getPlayerRole() == PlayerRole.BOWLER){
                                bowlers.add(matchPlayer.getPlayerStatsId());
                            }
                            return matchPlayer.getPlayerStatsId();
                        }
                ).collect(Collectors.toList());
        teamStats.setPlayerIds(matchPlayers);
        teamStats.setBowlerIds(bowlers);

        teamStats.reset();
        teamStatsRepository.save(teamStats);
        return teamStats;
    }

    public Match createMatch(Long id, TeamStats team1, TeamStats team2, int overs, String venue) {
        Match match = new Match();
        match.setMatchId(id);
        match.setTeam1Id(team1.getTeamStatsId());
        match.setTeam2Id(team2.getTeamStatsId());
        match.setVenue(venue);
        match.setOvers(overs);
        match.setMatchStatus(MatchStatus.UPCOMING);
        match.setUpdatedAt(LocalDateTime.now());

        team1.setMatchId(match.getMatchId());
        team2.setMatchId(match.getMatchId());

        matchRepository.save(match);
        return match;
    }


    public Inning simulateInning(Match match, TeamStats battingTeam, TeamStats bowlingTeam, int targetRuns, int overs) {

        Inning inning = new Inning();
        inning.setMatchId(match.getMatchId());
        inning.setBattingTeamId(battingTeam.getTeamStatsId());
        inning.setBowlingTeamId(bowlingTeam.getTeamStatsId());

        inningService.startInnings(inning, battingTeam, bowlingTeam, targetRuns, overs);
        battingTeam.setOvers(inning.getOvers());
        battingTeam.setWickets(inning.getWickets());

        return inning;
    }


    public void matchResult(Match match, Inning firstInnings, Inning secondInnings) {
        match.setMatchStatus(MatchStatus.COMPLETED);
        Result result = resultService.evaluateResult(firstInnings, secondInnings);
        if (result.getWinner() != null) {
            match.setWinnerId(result.getWinner().getTeamStatsId());
        }
        match.setWinningCondition(result.getWinningCondition());
    }
}