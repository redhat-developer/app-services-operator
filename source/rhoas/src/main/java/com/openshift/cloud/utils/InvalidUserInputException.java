package com.openshift.cloud.utils;

/** Exception raised when there is problem with validation of the CR */
public class InvalidUserInputException extends Exception {

  /**
   * Constructs a new exception with the specified detail message. The cause is not initialized, and
   * may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param field field that was invalid/undefined/not meeting
   */
  public InvalidUserInputException(String field, String additionalMessage) {
    super(String.format("Missing argument %s in CR, %s", field, additionalMessage));
  }
}
