package com.openshift.cloud.controllers;

import com.openshift.cloud.v1alpha.models.ManagedKafkaCondition;

public class ConditionAwareException extends Exception {
  private final ManagedKafkaCondition.Type type;
  private final ManagedKafkaCondition.Status status;
  private String reason, conditionMessage;

  public ConditionAwareException(
      String exceptionMessage,
      Throwable cause,
      ManagedKafkaCondition.Type type,
      ManagedKafkaCondition.Status status,
      String reason,
      String conditionMessage) {
    super(exceptionMessage, cause);
    this.type = type;
    this.status = status;
    this.reason = reason;
    this.conditionMessage = conditionMessage;
  }

  public ManagedKafkaCondition.Type getType() {
    return type;
  }

  public ManagedKafkaCondition.Status getStatus() {
    return status;
  }

  public String getReason() {
    return reason;
  }

  public String getConditionMessage() {
    return conditionMessage;
  }
}
