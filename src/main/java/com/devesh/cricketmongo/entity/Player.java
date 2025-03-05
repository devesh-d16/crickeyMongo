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
@Document(collection = "players")
public class Player {

    @Id
    private Long id;

    @Field("name")
    private String name;

    @Field("role")
    private PlayerRole role;

}
