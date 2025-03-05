package com.devesh.cricketmongo.utils;

import java.util.List;

public class GameUtil {

    public static final int MAX_PLAYERS = 11;
    public static final int BALLS_PER_OVER = 6;
    public static final List<Integer> SCORING_OPTIONS = List.of(0, 1, 2, 3, 4, 6, -1);
    public static final List<Integer> BATTER_WEIGHT = List.of(20, 25, 18, 8, 14, 10, 8);
    public static final List<Integer> BOWLER_WEIGHT = List.of(18, 22, 12, 5, 10, 6, 20);
}
