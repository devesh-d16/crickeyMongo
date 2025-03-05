package com.devesh.cricketmongo.repository;

import com.devesh.cricketmongo.entity.Match;
import com.devesh.cricketmongo.enums.MatchStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends MongoRepository<Match, String> {

    List<Match> findByMatchStatus(MatchStatus matchStatus);

    List<Match> findByTeam1IdOrTeam2Id(String team1Id, String team2Id);

    Match findByIdAndTeam1IdOrTeam2Id(String matchId, String team1Id, String team2Id);

   Optional<Match> findMatchById(Long id);


    List<Match> getMatchByWinnerId(String winnerId);

    List<Match> getMatchByVenue(String venue);

    List<Match> getMatchByMatchStatus(MatchStatus matchStatus);

    void deleteById(Long id);
}
