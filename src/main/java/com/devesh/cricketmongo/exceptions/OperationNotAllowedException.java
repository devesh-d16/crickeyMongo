package com.devesh.cricketmongo.exceptions;

public class OperationNotAllowedException extends RuntimeException {
  public OperationNotAllowedException(String message) {
    super(message);
  }
}
