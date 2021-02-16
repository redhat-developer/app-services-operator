package com.openshift.cloud.v1alpha.models;

import io.sundr.builder.annotations.Buildable;
import java.util.ArrayList;
import java.util.List;

@Buildable
public class ManagedServiceAccountRequestStatus {

  private String message = "";
  private String updated = "";
  private String serviceAccountSecretName = "";
  private List<ManagedKafkaCondition> conditions = new ArrayList<>();

  /** No args constructor for use in serialization */
  public ManagedServiceAccountRequestStatus() {}

  /**
   * @param serviceAccountSecretName
   * @param message
   * @param updated
   */
  public ManagedServiceAccountRequestStatus(
      String message, String updated, String serviceAccountSecretName) {
    super();
    this.message = message;
    this.updated = updated;
    this.serviceAccountSecretName = serviceAccountSecretName;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getUpdated() {
    return updated;
  }

  public void setUpdated(String updated) {
    this.updated = updated;
  }

  public String getServiceAccountSecretName() {
    return serviceAccountSecretName;
  }

  public void setServiceAccountSecretName(String serviceAccountSecretName) {
    this.serviceAccountSecretName = serviceAccountSecretName;
  }

  public List<ManagedKafkaCondition> getConditions() {
    return conditions;
  }

  public void setConditions(List<ManagedKafkaCondition> conditions) {
    this.conditions = conditions;
  }
}
