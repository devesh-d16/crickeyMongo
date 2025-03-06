package com.devesh.cricketmongo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "team_match_stats") // Define the MongoDB collection name
public class TeamStats {

    @Id
    private String teamStatsId;

    @Field("runs")
    private int runs = 0;

    @Field("wickets")
    private int wickets = 0;

    @Field("overs")
    private int overs = 0;

    @Field("winner")
    private boolean winner;

    @Field("team_id")
    private Long teamId; // team id

    @Field("match_id")
    private Long matchId; // Store only the match ID

    @Field("player_ids")
    private List<String> playerIds; //stats id

    @Field("bowler_ids")
    private List<String> bowlerIds; // stats id

    public void addRuns(int runs) {
        this.runs += runs;
    }

    public void incrementWickets() {
        this.wickets++;
    }

    public void reset() {
        this.runs = 0;
        this.wickets = 0;
        this.overs = 0;
    }
}
