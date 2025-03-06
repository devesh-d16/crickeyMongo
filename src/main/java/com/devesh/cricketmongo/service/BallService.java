package com.devesh.cricketmongo.service;

import com.devesh.cricketmongo.entity.*;
import com.devesh.cricketmongo.enums.PlayerRole;
import com.devesh.cricketmongo.model.StrikePair;
import com.devesh.cricketmongo.repository.BallRepository;
import com.devesh.cricketmongo.repository.PlayerStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BallService {

    private final GameRulesService gameRulesService;
    private final ScoringService scoringService;
    private final BallRepository ballRepository;
    private final PlayerService playerService;
    private final TeamStatService teamStatService;
    private final PlayerStatService playerStatService;
    private final PlayerStatsRepository playerStatsRepository;

    public void simulateBall(Inning inning, Ball ball, TeamStats batting, StrikePair strikePair, int targetRun, PlayerStats bowler){

        PlayerStats playerBatting = strikePair.playerOnStrike;
        playerBatting.incrementBallFaced();
        bowler.incrementBallsBowled();
        ball.setBatsmanId(playerBatting.getTeamId());
        int run = simulateRun(strikePair);
        handleRunOrWicket(inning, ball, run, batting, strikePair, targetRun, bowler);

        playerStatsRepository.save(bowler);
        playerStatsRepository.save(strikePair.playerOnStrike);
        gameRulesService.rotateStrike(run, strikePair);
    }

    public int simulateRun(StrikePair strikePair) {
        Player player = playerService.getPlayerById(strikePair.playerOnStrike.getPlayerId());
        return (player.getPlayerRole() == PlayerRole.BATTER)
                ? scoringService.getRandomBatterWeightScore()
                : scoringService.getRandomBowlerWeightScore();
    }

    public void handleRunOrWicket(Inning inning, Ball ball, int run, TeamStats batting, StrikePair strikePair, int targetRun, PlayerStats bowler) {
        if (run == -1) {
            handleWicket(inning, ball, batting, strikePair, targetRun);
        } else {
            handleRun(inning, ball, run, batting, strikePair, bowler);
        }
    }

    public void handleWicket(Inning inning, Ball ball, TeamStats batting, StrikePair strikePair, int targetRun) {
        inning.incrementWickets();
        batting.incrementWickets();

        ball.setRuns(0);
        ball.setWicket(true);

        if (!gameRulesService.gameEnd(batting, inning, targetRun) && strikePair.getNextBat() <= 10) {
            List<PlayerStats> players = batting.getPlayerIds().stream().map(
                    playerStatService::findById
            ).collect(Collectors.toList());
            strikePair.playerOnStrike = (players.get(strikePair.getNextBat()));
            strikePair.nextBatsman();
        }
    }

    public void handleRun(Inning inning, Ball ball, int run, TeamStats batting, StrikePair strikePair, PlayerStats bowler) {
        ball.setRuns(run);

        batting.addRuns(run);
        inning.addRuns(run);
        bowler.addRunsConceded(run);

        strikePair.playerOnStrike.addRuns(run);
    }

    public void deleteAllBallById(List<String> ballIds) {
        for(String ballId : ballIds){
            ballRepository.deleteByBallId(ballId);
        }
    }
}