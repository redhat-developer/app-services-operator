package com.openshift.cloud.controllers;

import com.openshift.cloud.v1alpha.models.CloudServiceCondition;

public class ConditionAwareException extends Exception {

  private static final long serialVersionUID = 18383451L;

  private final CloudServiceCondition.Type type;
  private final CloudServiceCondition.Status status;
  private final String reason, conditionMessage;

  public ConditionAwareException(String exceptionMessage, Throwable cause,
      CloudServiceCondition.Type type, CloudServiceCondition.Status status, String reason,
      String conditionMessage) {
    super(exceptionMessage, cause);
    this.type = type;
    this.status = status;
    this.reason = reason;
    this.conditionMessage = conditionMessage;
  }

  public CloudServiceCondition.Type getType() {
    return type;
  }

  public CloudServiceCondition.Status getStatus() {
    return status;
  }

  public String getReason() {
    return reason;
  }

  public String getConditionMessage() {
    return conditionMessage;
  }
}
