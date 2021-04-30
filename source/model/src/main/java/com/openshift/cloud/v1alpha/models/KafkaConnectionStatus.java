package com.openshift.cloud.v1alpha.models;

import io.sundr.builder.annotations.Buildable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Buildable(builderPackage = "io.fabric8.kubernetes.api.builder", editableEnabled = false)
public class KafkaConnectionStatus {
  private String message;
  private String updated;
  private String bootstrapServerHost;
  private String serviceAccountSecretName;
  private List<KafkaCondition> conditions;
  private Map<String, String> metadata;

 

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
 
}
