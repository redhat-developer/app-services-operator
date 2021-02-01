package com.openshift.cloud.v1alpha.models;


public class ManagedServiceAccountRequestStatus {

  private String message;
  private String updated;
  private String serviceAccountSecretName;

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
}
