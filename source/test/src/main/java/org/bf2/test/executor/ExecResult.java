package org.bf2.test.executor;

import java.io.Serializable;

public class ExecResult implements Serializable {

  private static final long serialVersionUID = 1L;

  private final int returnCode;
  private final String stdOut;
  private final String stdErr;

  ExecResult(int returnCode, String stdOut, String stdErr) {
    this.returnCode = returnCode;
    this.stdOut = stdOut;
    this.stdErr = stdErr;
  }

  public boolean exitStatus() {
    return returnCode == 0;
  }

  public int returnCode() {
    return returnCode;
  }

  public String out() {
    return stdOut;
  }

  public String err() {
    return stdErr;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ExecResult{");
    sb.append("returnCode=").append(returnCode);
    sb.append(", stdOut='").append(stdOut).append('\'');
    sb.append(", stdErr='").append(stdErr).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
