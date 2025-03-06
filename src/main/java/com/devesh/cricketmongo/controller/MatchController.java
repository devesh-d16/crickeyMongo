package com.devesh.cricketmongo.controller;


import com.devesh.cricketmongo.dao.MatchDAO;
import com.devesh.cricketmongo.dto.MatchRequestDTO;
import com.devesh.cricketmongo.entity.Match;
import com.devesh.cricketmongo.service.MatchService;
import com.devesh.cricketmongo.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchDAO matchDAO;
    private final MatchService matchService;
    private final Mapper mapper;

    // done
    @PostMapping("/start")
    public ResponseEntity<?> startMatch(@RequestBody MatchRequestDTO matchRequestDTO){
       return new ResponseEntity<>(matchService.startMatch(matchRequestDTO), HttpStatus.CREATED);
    }

    // done
    @GetMapping
    public ResponseEntity<?> getAllMatches(){
        List<Match> matches = matchDAO.getAllMatches();
        return new ResponseEntity<>(mapper.convertToList(matches), HttpStatus.OK);
    }

    // done
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getMatchById(@PathVariable Long id){
        Match match = matchDAO.getMatchById(id);
        return new ResponseEntity<>( mapper.getMatchResponse(match), HttpStatus.OK);
    }

    // done
    @GetMapping("/venue")
    public ResponseEntity<?> getMatchByVenue(@RequestParam String venue){
        List<Match> matches = matchDAO.getMatchesByVenue(venue);
        return new ResponseEntity<>(mapper.convertToList(matches), HttpStatus.OK);
    }

    // done
    @GetMapping("/status")
    public ResponseEntity<?> getMatchByStatus(@RequestParam String matchStatus){
        List<Match> matches = matchDAO.getMatchesByMatchStatus(matchStatus);
        return new ResponseEntity<>(mapper.convertToList(matches), HttpStatus.OK);
    }

    // done
    @GetMapping("/{id}/winner")
    public ResponseEntity<?> getWinnerOfMatchById(@PathVariable Long id){
        return new ResponseEntity<>(matchDAO.getWinnerByMatchId(id), HttpStatus.OK);
    }

    // done
    @GetMapping("/teams/name/{name}")
    public ResponseEntity<?> getMatchByTeamId(@PathVariable String name){
        List<Match> matchList = matchDAO.getMatchByTeamName(name);
        return new ResponseEntity<>(mapper.convertToList(matchList), HttpStatus.OK);
    }


    @GetMapping("/teams/name")
    public ResponseEntity<?> getMatchByTeamPlayed(@RequestParam String team1Name, @RequestParam String team2Name){
        List<Match> matches = matchDAO.getMatchByTeamPlayedName(team1Name, team2Name);
        return new ResponseEntity<>(mapper.convertToList(matches), HttpStatus.OK);
    }


    @GetMapping("/teams/{name}/winner")
    public ResponseEntity<?> getMatchByWinnerTeamId(@PathVariable String name){
        List<Match> matches = matchDAO.getMatchByWinnerTeamName(name);
        return new ResponseEntity<>(mapper.convertToList(matches), HttpStatus.OK);
    }


    // done
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteMatchById(@PathVariable Long id){
        matchDAO.deleteMatchById(id);
        return ResponseEntity.noContent().build();
    }
}
