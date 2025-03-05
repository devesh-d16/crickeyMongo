package com.devesh.cricketmongo.repository;

import com.devesh.cricketmongo.entity.Ball;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BallRepository extends MongoRepository<Ball, Long> {
    void deleteAllByOverId(String overId);

    List<Ball> findAllByOverId(String overId);

    void deleteByOverId(String overId);

    Ball getBallById(String id);

    void deleteById(String id);
}
