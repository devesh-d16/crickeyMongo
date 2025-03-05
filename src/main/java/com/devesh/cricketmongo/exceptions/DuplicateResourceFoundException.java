package com.devesh.cricketmongo.exceptions;

public class DuplicateResourceFoundException extends RuntimeException {
  public DuplicateResourceFoundException(String message) {
    super(message);
  }
}
