package com.devesh.cricketmongo.entity;

import com.devesh.cricketmongo.enums.MatchStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "matches")
public class Match {

    @Id
    private Long id;

    @Field("match_status")
    private MatchStatus matchStatus;

    @Field("overs")
    private int overs;

    private String venue;

    private LocalDateTime updatedAt;

    @Field("winning_condition")
    private String winningCondition;

    @Field("team1_id")
    private String team1Id; // stats id

    @Field("team2_id")
    private String team2Id; // stats id

    @Field("innings")
    private List<String> inningIds;

    @Field("winner_id")
    private String winnerId; // stats id
}
