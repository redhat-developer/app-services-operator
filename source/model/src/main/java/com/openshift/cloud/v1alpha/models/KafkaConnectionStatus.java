package com.openshift.cloud.v1alpha.models;

import io.sundr.builder.annotations.Buildable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Buildable
public class KafkaConnectionStatus {
  private String message;
  private String updated;
  private String bootstrapServerHost;
  private String serviceAccountSecretName;
  private List<KafkaCondition> conditions;
  private Map<String, String> metadata;

  /** No args constructor for use in serialization */
  public KafkaConnectionStatus() {}

  /**
   * @param serviceAccountSecretName
   * @param message
   * @param bootstrapServerHost
   * @param updated
   */
  public KafkaConnectionStatus(
      String message, String updated, String bootstrapServerHost, String serviceAccountSecretName) {
    super();
    this.message = message;
    this.updated = updated;
    this.bootstrapServerHost = bootstrapServerHost;
    this.serviceAccountSecretName = serviceAccountSecretName;
    this.metadata = new HashMap<String, String>();
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

  public String getBootstrapServerHost() {
    return bootstrapServerHost;
  }

  public void setBootstrapServerHost(String bootstrapServerHost) {
    this.bootstrapServerHost = bootstrapServerHost;
  }

  public Map<String, String> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, String> metadata) {
    this.metadata = metadata;
  }
}
