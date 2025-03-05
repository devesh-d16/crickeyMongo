package com.devesh.cricketmongo.entity;

import com.devesh.cricketmongo.enums.PlayerRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "players_match_stats")
public class PlayerStats {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("runsScored")
    private int runsScored = 0;

    @Field("ballsFaced")
    private int ballsFaced;

    @Field("wicketsTaken")
    private int wicketsTaken = 0;

    @Field("ballsBowled")
    private int ballsBowled = 0;

    @Field("runsConceded")
    private int runsConceded = 0;

    @Field("player_id")
    private Long playerId; // players id

    @Field("team_id")
    private String teamId; // stats id

    public void addRuns(int runs) {
        this.runsScored += runs;
    }

    public void incrementBallFaced(){
        this.ballsFaced++;
    }

    public void incrementWicketTaken() {
        this.wicketsTaken++;
    }

    public void incrementBallsBowled() {
        this.ballsBowled++;
    }

    public void addRunsConceded(int run) {
        this.runsConceded += run;
    }
}
