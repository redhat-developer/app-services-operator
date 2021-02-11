package com.openshift.cloud.v1alpha.models;

import io.sundr.builder.annotations.Buildable;
import java.util.List;

@Buildable
public class ManagedKafkaConnectionStatus {

  private String message;
  private String updated;
  private BoostrapServer boostrapServer;
  private String serviceAccountSecretName;
  private String saslMechanism;
  private String securityProtocol;
  private List<ManagedKafkaCondition> conditions;

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
    this.saslMechanism = "PLAIN";
    this.securityProtocol = "SSL";
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

  public String getSecurityProtocol() {
    return securityProtocol;
  }

  public void setSecurityProtocol(String securityProtocol) {
    this.securityProtocol = securityProtocol;
  }

  public String getSaslMechanism() {
    return saslMechanism;
  }

  public void setSaslMechanism(String saslMechanism) {
    this.saslMechanism = saslMechanism;
  }

  public List<ManagedKafkaCondition> getConditions() {
    return conditions;
  }

  public void setConditions(List<ManagedKafkaCondition> conditions) {
    this.conditions = conditions;
  }
}
