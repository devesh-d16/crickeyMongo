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
@Document(collection = "overs")
public class Over {

    @Id
    private String overId;

    @Field("over_no")
    private int overNo;

    @Field("runs")
    private int runs = 0;

    @Field("wickets")
    private int wickets = 0;

    @Field("inning_id")
    private String inningId;

    @Field("bowler_id")
    private String bowlerId; // stats id

    @Field("ball_ids")
    private List<String> ballIds;

    public void addRuns(int runs) {
        this.runs += runs;
    }

    public void addWicket() {
        this.wickets++;
    }
}
