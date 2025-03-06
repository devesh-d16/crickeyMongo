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
@Document(collection = "innings")
public class Inning {

    @Id
    private String inningId;

    @Field("runs")
    private int runs = 0;

    @Field("wickets")
    private int wickets = 0;

    @Field("overs")
    private int overs = 0;

    @Field("match_id")
    private Long matchId;

    @Field("batting_team_id")
    private String battingTeamId; // stats id

    @Field("bowling_team_id")
    private String bowlingTeamId; // stats id

    @Field("all_overs")
    private List<String> overIds;

    public void addRuns(int runs) {
        this.runs += runs;
    }

    public void incrementWickets() {
        this.wickets++;
    }

    public void addOvers() {
        this.overs++;
    }
}
