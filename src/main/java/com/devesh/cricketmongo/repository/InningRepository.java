package com.devesh.cricketmongo.repository;

import com.devesh.cricketmongo.entity.Inning;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InningRepository extends MongoRepository<Inning, Long> {

    Inning getInningById(String id);

    List<Inning> findAllByMatchId(Long matchId);

    void deleteByMatchId(Long matchId);

    void deleteById(String id);
}
