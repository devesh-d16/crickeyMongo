package com.devesh.cricketmongo.service;

import com.devesh.cricketmongo.entity.*;
import com.devesh.cricketmongo.model.StrikePair;
import com.devesh.cricketmongo.repository.InningRepository;
import com.devesh.cricketmongo.repository.OverRepository;
import com.devesh.cricketmongo.repository.PlayerStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InningService {

    private final OverService overService;
    private final GameRulesService gameRulesService;
    private final PlayerStatService playerStatService;
    private final InningRepository inningRepository;
    private final OverRepository overRepository;
    private final PlayerStatsRepository playerStatsRepository;

    public void startInnings(Inning inning, TeamStats battingTeam, TeamStats bowlingTeam, int targetRun, int over) {
        List<PlayerStats> playersBat= battingTeam.getPlayerIds().stream().map(
                playerIds -> playerStatService.findById(playerIds)
        ).collect(Collectors.toList());

        List<PlayerStats> playersBowl = battingTeam.getBowlerIds().stream().map(
                playerIds -> playerStatService.findById(playerIds)
        ).collect(Collectors.toList());

        StrikePair strikePair = new StrikePair(playersBat.get(0), playersBat.get(1));

        List<Over> overs = new ArrayList<>();
        int bowlerIndex = 0;

        for (int overNumber = 1; overNumber <= over && !gameRulesService.gameEnd(battingTeam, inning, targetRun); overNumber++) {
            PlayerStats bowler = getNextBowler(playersBowl, bowlerIndex);
            Over newOver = createOver(inning, overNumber, bowler);

            overService.simulateOver(inning, newOver, battingTeam, targetRun, strikePair, bowler);

            overs.add(newOver);
            inning.addOvers();
            overRepository.save(newOver);

            bowlerIndex = (bowlerIndex + 1) % playersBowl.size();
        }

        inning.setOverIds(overs.stream().map(Over::getOverId).collect(Collectors.toList()));
        inningRepository.save(inning);
    }

    private PlayerStats getNextBowler(List<PlayerStats> bowlingTeam, int index) {
        return bowlingTeam.get(index);
    }

    private Over createOver(Inning inning, int overNumber, PlayerStats bowler) {
        Over over = new Over();
        over.setOverNo(overNumber);
        over.setInningId(inning.getInningId());
        over.setBowlerId(bowler.getPlayerStatsId());
        return over;
    }

    public Inning getInningById(String id){
        return inningRepository.findByInningId(id);
    }


    public void deleteAllInningsById(List<String> innings) {
        for(String inningId : innings){
            Inning inning = inningRepository.findByInningId(inningId);
            List<String> overIds = inning.getOverIds();
            overService.deleteAllOversById(overIds);
            inningRepository.deleteByInningId(inningId);
        }
    }
}
