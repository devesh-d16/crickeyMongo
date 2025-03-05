package com.devesh.cricketmongo.controller;


import com.devesh.cricketmongo.entity.Team;
import com.devesh.cricketmongo.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody Team team) {
        return new ResponseEntity<>(teamService.createTeam(team), HttpStatus.CREATED);
    }
    @GetMapping("/id/{teamId}")
    public ResponseEntity<?> getTeamById(@PathVariable Long teamId){
        return new ResponseEntity<>(teamService.getTeamById(teamId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllTeams() {
        return new ResponseEntity<>(teamService.getAllTeam(), HttpStatus.OK);
    }

    @GetMapping("/name/{teamName}")
    public ResponseEntity<?> getTeamByName(@PathVariable String teamName) {
        return new ResponseEntity<>(teamService.getTeamByName(teamName), HttpStatus.OK);
    }

    @PutMapping("/id/{teamId}")
    public ResponseEntity<?> updateTeam(@PathVariable Long teamId, @RequestBody Team team) {
        return new ResponseEntity<>(teamService.updateTeam(teamId, team), HttpStatus.OK);
    }

    @PostMapping("/addAll")
    public ResponseEntity<?> addTeams(@RequestBody List<Team> teams){
        teamService.addTeams(teams);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
