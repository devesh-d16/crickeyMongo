package com.devesh.cricketmongo.service;

import com.devesh.cricketmongo.entity.*;
import com.devesh.cricketmongo.model.StrikePair;
import com.devesh.cricketmongo.repository.BallRepository;
import com.devesh.cricketmongo.repository.OverRepository;
import com.devesh.cricketmongo.utils.GameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OverService {

    private final BallService ballService;
    private final GameRulesService gameRulesService;
    private final OverRepository overRepository;
    private final BallRepository ballRepository;


    public void simulateOver(Inning inning, Over over, TeamStats batting, int targetRun, StrikePair strikePair, PlayerStats bowler) {
        List<Ball> balls = new ArrayList<>();

        for (int ballNumber = 1; ballNumber <= GameUtil.BALLS_PER_OVER && !gameRulesService.gameEnd(batting, inning, targetRun); ballNumber++) {
            Ball newBall = createBall(over, ballNumber, bowler);
            ballService.simulateBall(inning, newBall, batting, strikePair, targetRun, bowler);

            handleWicket(newBall, over, bowler);
            over.addRuns(newBall.getRuns());

            ballRepository.save(newBall);
            balls.add(newBall);
        }

        gameRulesService.swapStrikers(strikePair);
        over.setBallIds(balls.stream().map(Ball::getBallId).collect(Collectors.toList()));
    }

    private Ball createBall(Over over, int ballNumber, PlayerStats bowler) {
        Ball ball = new Ball();
        ball.setOverId(over.getOverId());
        ball.setBallNo(ballNumber);
        ball.setBowlerId(bowler.getPlayerStatsId());
        return ball;
    }

    private void handleWicket(Ball ball, Over over, PlayerStats bowler) {
        if (ball.isWicket()) {
            over.addWicket();
            bowler.incrementWicketTaken();
        }
    }



    public void deleteAllOversById(List<String> overIds) {
        for(String overId : overIds){
            Over over = overRepository.findByOverId(overId);
            List<String> ballIds = over.getBallIds();
            ballService.deleteAllBallById(ballIds);
            overRepository.deleteByOverId(overId);
        }
    }
}