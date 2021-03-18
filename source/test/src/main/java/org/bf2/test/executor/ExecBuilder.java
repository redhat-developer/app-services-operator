package org.bf2.test.executor;

import io.fabric8.kubernetes.api.model.EnvVar;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ExecBuilder {

  private String input;
  private List<String> command;
  private Set<EnvVar> envVars;
  private int timeout;
  private boolean logToOutput;
  private boolean throwErrors;

  public ExecBuilder withCommand(List<String> command) {
    this.command = command;
    return this;
  }

  public ExecBuilder withCommand(String... cmd) {
    this.command = Arrays.asList(cmd);
    return this;
  }

  public ExecBuilder withEnvVars(Set<EnvVar> envVars) {
    this.envVars = envVars;
    return this;
  }

  public ExecBuilder withInput(String input) {
    this.input = input;
    return this;
  }

  public ExecBuilder logToOutput(boolean logToOutput) {
    this.logToOutput = logToOutput;
    return this;
  }

  public ExecBuilder throwErrors(boolean throwErrors) {
    this.throwErrors = throwErrors;
    return this;
  }

  public ExecBuilder timeout(int timeout) {
    this.timeout = timeout;
    return this;
  }

  public ExecResult exec() {
    return Exec.exec(input, command, envVars, timeout, logToOutput, throwErrors);
  }
}
