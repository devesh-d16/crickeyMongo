package com.devesh.cricketmongo.controller;


import com.devesh.cricketmongo.dao.MatchDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scoreboard")
@RequiredArgsConstructor
public class ScoreboardController {

    private final MatchDAO matchDAO;

    @GetMapping("/match/{id}")
    public ResponseEntity<?> getScoreboardByMatchId(@PathVariable Long id) {
        return new ResponseEntity<>(matchDAO.getMatchScoreboard(id), HttpStatus.OK);
    }
}
