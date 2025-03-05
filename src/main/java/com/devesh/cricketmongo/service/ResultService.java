package com.devesh.cricketmongo.service;


import com.devesh.cricketmongo.entity.Inning;
import com.devesh.cricketmongo.entity.Team;
import com.devesh.cricketmongo.entity.TeamStats;
import com.devesh.cricketmongo.model.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final TeamStatService teamStatService;
    private final TeamService teamService;

    public Result evaluateResult(Inning firstInnings, Inning secondInnings) {
        Result result = new Result();

        int runsScoredByTeam1 = firstInnings.getRuns();
        int runsScoredByTeam2 = secondInnings.getRuns();

        if(runsScoredByTeam1 > runsScoredByTeam2){
            int runs = runsScoredByTeam1 - runsScoredByTeam2;
            TeamStats winnerTeam = teamStatService.findByTeamStatId(firstInnings.getBattingTeamId());
            Team winner = teamService.getTeamById(winnerTeam.getTeamId());
            result.setWinner(winnerTeam);
            result.setWinningMargin(runs);
            result.setWinningCondition(winner.getName() + " won the game by " + runs + " runs.");
        }
        else if(runsScoredByTeam1 < runsScoredByTeam2){
            int wicketsMargin = 10 - secondInnings.getWickets();
            TeamStats winnerTeam = teamStatService.findByTeamStatId(secondInnings.getBattingTeamId());
            Team winner = teamService.getTeamById(winnerTeam.getTeamId());
            result.setWinner(winnerTeam);
            result.setWinningMargin(wicketsMargin);
            result.setWinningCondition(winner.getName()+ " won the game by " + wicketsMargin + " wickets.");
        }
        else{
            result.setWinner(null);
            result.setWinningMargin(0);
            result.setWinningCondition("Match drawn");
        }
        return result;
    }
}
