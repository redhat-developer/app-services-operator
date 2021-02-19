package com.openshift.cloud.v1alpha.models;

public class ManagedKafkaCondition {
  public enum Type {
    AcccesTokenSecretValid,
    UserKafkasUpToDate,
    ServiceAccountCreated,
    ServiceAccountSecretCreated,
    Finished,
    FoundKafkaById;
  }

  public enum Status {
    True,
    False,
    Unknown
  }

  private Type type;
  private String reason;
  private String message;
  private Status status;
  private String lastTransitionTime;

  public Type getType() {
    return type;
  }

  public String getReason() {
    return reason;
  }

  public String getMessage() {
    return message;
  }

  public Status getStatus() {
    return status;
  }

  public String getLastTransitionTime() {
    return lastTransitionTime;
  }

  public ManagedKafkaCondition setType(String type) {
    if ("AcccesTokenSecretAvailable".equalsIgnoreCase(type)) {
      // Old value from before release, keeping for migration from 0.1.x
      this.type = Type.AcccesTokenSecretValid;
      return this;
    }
    this.type = Type.valueOf(type);
    return this;
  }

  public ManagedKafkaCondition setType(Type type) {
    this.type = type;
    return this;
  }

  public ManagedKafkaCondition setReason(String reason) {
    this.reason = reason;
    return this;
  }

  public ManagedKafkaCondition setMessage(String message) {
    this.message = message;
    return this;
  }

  public ManagedKafkaCondition setStatus(Status status) {
    this.status = status;
    return this;
  }

  public ManagedKafkaCondition setStatus(String status) {
    this.status = Status.valueOf(status);
    return this;
  }

  public ManagedKafkaCondition setLastTransitionTime(String lastTransitionTime) {
    this.lastTransitionTime = lastTransitionTime;
    return this;
  }
}
