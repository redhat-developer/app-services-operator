package com.openshift.cloud.v1alpha.models;

import io.sundr.builder.annotations.Buildable;
import java.util.ArrayList;
import java.util.List;

@Buildable(builderPackage = "io.fabric8.kubernetes.api.builder", editableEnabled = false)
public class CloudServiceAccountRequestStatus {

  private String message = "";
  private String updated = "";
  private String serviceAccountSecretName = "";
  private List<KafkaCondition> conditions = new ArrayList<>();

  /** No args constructor for use in serialization */
  public CloudServiceAccountRequestStatus() {}

  /**
   * @param serviceAccountSecretName
   * @param message
   * @param updated
   */
  public CloudServiceAccountRequestStatus(
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

  public List<KafkaCondition> getConditions() {
    return conditions;
  }

  public void setConditions(List<KafkaCondition> conditions) {
    this.conditions = conditions;
  }
}
