package org.bf2.test;

public class WaitException extends RuntimeException {
  public WaitException(String message) {
    super(message);
  }

  public WaitException(Throwable cause) {
    super(cause);
  }
}
