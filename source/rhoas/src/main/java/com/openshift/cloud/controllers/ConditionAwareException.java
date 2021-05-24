package com.openshift.cloud.controllers;

import com.openshift.cloud.v1alpha.models.KafkaCondition;

public class ConditionAwareException extends Exception {

  private static final long serialVersionUID = 18383451L;

  private final KafkaCondition.Type type;
  private final KafkaCondition.Status status;
  private String reason, conditionMessage;

  public ConditionAwareException(String exceptionMessage, Throwable cause, KafkaCondition.Type type,
      KafkaCondition.Status status, String reason, String conditionMessage) {
    super(exceptionMessage, cause);
    this.type = type;
    this.status = status;
    this.reason = reason;
    this.conditionMessage = conditionMessage;
  }

  public KafkaCondition.Type getType() {
    return type;
  }

  public KafkaCondition.Status getStatus() {
    return status;
  }

  public String getReason() {
    return reason;
  }

  public String getConditionMessage() {
    return conditionMessage;
  }
}
