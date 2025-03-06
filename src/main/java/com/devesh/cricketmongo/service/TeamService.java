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
        if(team.getTeamName() == null){
            throw new InvalidRequestException("Team name is required");
        }
        try {
            Team newTeam = new Team();
            newTeam.setTeamName(team.getTeamName().toUpperCase());
            return teamRepository.save(team);
        }
        catch (DataAccessException e) {
            throw new SystemException("Error while creating team '" + team.getTeamId() + "'. Please try again.");
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
        return teamRepository.findTeamByTeamId((teamId)).orElseThrow(() -> new ResourceNotFoundException("Team with id " + teamId + " not found."));
    }

    public Team getTeamByName(String teamName) {
        Team team = teamRepository.findByTeamName(teamName).orElseThrow(() -> new ResourceNotFoundException("Team with name " + teamName + " not found."));
        if (team == null) {
            throw new ResourceNotFoundException("Team with name " + teamName + " not found.");
        }
        return team;
    }

    public Team updateTeam(Long teamId, Team team) {
        if(team.getTeamName() == null){
            throw new InvalidRequestException("Team name is required.");
        }
        Team updateTeam = teamRepository.findTeamByTeamId(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team with id " + teamId + " not found."));

        if(teamRepository.existsByTeamName(team.getTeamName()) && !updateTeam.getTeamName().equals(team.getTeamName())){
            throw new DuplicateResourceFoundException("Team name '" + team.getTeamName() + "' already exists.");
        }

        updateTeam.setTeamName(team.getTeamName());
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
