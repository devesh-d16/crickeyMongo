package com.devesh.cricketmongo.controller;


import com.devesh.cricketmongo.entity.Player;
import com.devesh.cricketmongo.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        Player newPlayer = playerService.createPlayer(player);
        return new ResponseEntity<>(newPlayer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllPlayer() {
        return new ResponseEntity<>(playerService.getAllPlayers(), HttpStatus.OK);
    }

    @GetMapping("/id/{playerId}")
    public ResponseEntity<?> getPlayerById(@PathVariable Long playerId){
        return new ResponseEntity<>(playerService.getPlayerById(playerId), HttpStatus.OK);
    }

    @GetMapping("/name/{teamName}")
    public ResponseEntity<?> getPlayerByName(@PathVariable String teamName){
        return new ResponseEntity<>(playerService.getPlayerByName(teamName) , HttpStatus.OK);
    }

    @GetMapping("/role")
    public ResponseEntity<?> getPlayerByRole(@RequestParam String playerRole){
        return new ResponseEntity<>(playerService.getPlayerByRole(playerRole), HttpStatus.OK);
    }

    @PutMapping("/id/{playerId}")
    public ResponseEntity<?> updatePlayer(@PathVariable Long playerId, @RequestBody Player player) {
        Player updatedPlayer = playerService.updatePlayer(playerId, player);
        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }

    @PostMapping("/addAll")
    public ResponseEntity<?> addPlayers(@RequestBody List<Player> players){
        playerService.addPlayers(players);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
