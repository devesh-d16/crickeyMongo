package com.devesh.cricketmongo.repository;

import com.devesh.cricketmongo.entity.Over;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OverRepository extends MongoRepository<Over, Long> {
    List<Over> findAllByInningId(String inningId);

    void deleteByOverId(String overId);

    Over findByOverId(String overId);
}
