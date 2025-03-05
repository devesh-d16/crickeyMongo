package com.devesh.cricketmongo.exceptions;

public class GameRuleException extends RuntimeException {
    public GameRuleException(String message) {
        super(message);
    }
}
