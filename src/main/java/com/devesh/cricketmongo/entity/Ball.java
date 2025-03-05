package com.devesh.cricketmongo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "balls")
public class Ball {

    @Id
    private String id;

    private int ballNo;
    private int runs;

    @Field("isWicket")
    private boolean isWicket;

    @Field("over_id")
    private String overId;

    @Field("batsman_id")
    private String batsmanId; // stats id

    @Field("bowler_id")
    private String bowlerId; // stats id
}
