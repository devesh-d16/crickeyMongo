package com.devesh.cricketmongo.service;

import com.devesh.cricketmongo.entity.Team;
import com.devesh.cricketmongo.exceptions.DuplicateResourceFoundException;
import com.devesh.cricketmongo.exceptions.InvalidRequestException;
import com.devesh.cricketmongo.exceptions.ResourceNotFoundException;
import com.devesh.cricketmongo.exceptions.SystemException;
import com.devesh.cricketmongo.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public Team createTeam(Team team) {
        if(team.getName() == null){
            throw new InvalidRequestException("Team name is required");
        }
        try {
            Team newTeam = new Team();
            newTeam.setName(team.getName().toUpperCase());
            return teamRepository.save(team);
        }
        catch (DataAccessException e) {
            throw new SystemException("Error while creating team '" + team.getId() + "'. Please try again.");
        }
        catch (Exception e) {
            throw new SystemException("Unexpected error occurred. Please contact support.");
        }
    }

    public List<Team> getAllTeam() {
        List<Team> teams = teamRepository.findAll();
        if (teams.isEmpty()) {
            return Collections.emptyList();
        }
        return teams;
    }

    public Team getTeamById(Long teamId) {
        return teamRepository.getTeamById((teamId)).orElseThrow(() -> new ResourceNotFoundException("Team with id " + teamId + " not found."));
    }

    public Team getTeamByName(String teamName) {
        Team team = teamRepository.findByName(teamName).orElseThrow(() -> new ResourceNotFoundException("Team with name " + teamName + " not found."));
        if (team == null) {
            throw new ResourceNotFoundException("Team with name " + teamName + " not found.");
        }
        return team;
    }

    public Team updateTeam(Long teamId, Team team) {
        if(team.getName() == null){
            throw new InvalidRequestException("Team name is required.");
        }
        Team updateTeam = teamRepository.getTeamById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team with id " + teamId + " not found."));

        if(teamRepository.existsByName(team.getName()) && !updateTeam.getName().equals(team.getName())){
            throw new DuplicateResourceFoundException("Team name '" + team.getName() + "' already exists.");
        }

        updateTeam.setName(team.getName());
        return teamRepository.save(updateTeam);
    }

//    public String deleteTeamById(Long teamId) {
//        teamRepository.deleteById(teamId);
//        return ("Team with id " + teamId + " deleted successfully");
//    }


    public void addTeams(List<Team> teams) {
        for (Team team : teams ){
            createTeam(team);
        }
    }
}
