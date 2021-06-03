package com.openshift.cloud.v1alpha.models;

// TODO This needs to be renamed because we're more than just Kafkas now
public class KafkaCondition {
  public enum Type {
    AcccesTokenSecretValid, UserKafkasUpToDate, ServiceRegistriesUpToDate, ServiceAccountCreated, ServiceAccountSecretCreated, Finished, FoundKafkaById, FoundServiceRegistryById;
  }

  public enum Status {
    True, False, Unknown
  }

  private Type type;
  private String reason;
  private String message;
  private Status status;
  private String lastTransitionTime;
  private Long lastTransitionGeneration;

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

  public KafkaCondition setType(String type) {
    if ("AcccesTokenSecretAvailable".equalsIgnoreCase(type)) {
      // Old value from before release, keeping for migration from 0.1.x
      this.type = Type.AcccesTokenSecretValid;
      return this;
    }
    this.type = Type.valueOf(type);
    return this;
  }

  public KafkaCondition setType(Type type) {
    this.type = type;
    return this;
  }

  public KafkaCondition setReason(String reason) {
    this.reason = reason;
    return this;
  }

  public KafkaCondition setMessage(String message) {
    this.message = message;
    return this;
  }

  public KafkaCondition setStatus(Status status) {
    this.status = status;
    return this;
  }

  public KafkaCondition setStatus(String status) {
    this.status = Status.valueOf(status);
    return this;
  }

  public KafkaCondition setLastTransitionTime(String lastTransitionTime) {
    this.lastTransitionTime = lastTransitionTime;
    return this;
  }

  public Long getLastTransitionGeneration() {
    return lastTransitionGeneration;
  }

  public KafkaCondition setLastTransitionGeneration(Long lastTransitionGeneration) {
    this.lastTransitionGeneration = lastTransitionGeneration;
    return this;
  }
}
