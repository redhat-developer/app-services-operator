package com.openshift.cloud.v1alpha.models;

import java.util.HashMap;
import java.util.Map;

public class ManagedKafkaConnectionStatus {

  private String message;
  private String updated;
  private BoostrapServer boostrapServer;
  private String serviceAccountSecretName;
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  /** No args constructor for use in serialization */
  public ManagedKafkaConnectionStatus() {}

  /**
   * @param serviceAccountSecretName
   * @param message
   * @param boostrapServer
   * @param updated
   */
  public ManagedKafkaConnectionStatus(
      String message,
      String updated,
      BoostrapServer boostrapServer,
      String serviceAccountSecretName) {
    super();
    this.message = message;
    this.updated = updated;
    this.boostrapServer = boostrapServer;
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

  public BoostrapServer getBoostrapServer() {
    return boostrapServer;
  }

  public void setBoostrapServer(BoostrapServer boostrapServer) {
    this.boostrapServer = boostrapServer;
  }

  public String getServiceAccountSecretName() {
    return serviceAccountSecretName;
  }

  public void setServiceAccountSecretName(String serviceAccountSecretName) {
    this.serviceAccountSecretName = serviceAccountSecretName;
  }

  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }
}
