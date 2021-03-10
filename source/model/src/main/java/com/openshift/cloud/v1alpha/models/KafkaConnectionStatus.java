package com.openshift.cloud.v1alpha.models;

import io.sundr.builder.annotations.Buildable;
import java.util.List;

@Buildable
public class KafkaConnectionStatus {

  private static final String UI_REF_TEMPLATE =
      "https://cloud.redhat.com/beta/application-services/openshift-streams/kafkas/%s";

  private String message;
  private String updated;
  private String bootstrapServerHost;
  private String serviceAccountSecretName;
  private String saslMechanism;
  private String securityProtocol;
  private String uiRef;
  private List<KafkaCondition> conditions;

  /** No args constructor for use in serialization */
  public KafkaConnectionStatus() {}

  /**
   * @param serviceAccountSecretName
   * @param message
   * @param boostrapServer
   * @param updated
   */
  public KafkaConnectionStatus(
      String message, String updated, String bootstrapServerHost, String serviceAccountSecretName) {
    super();
    this.message = message;
    this.updated = updated;
    this.bootstrapServerHost = bootstrapServerHost;
    this.serviceAccountSecretName = serviceAccountSecretName;
    this.saslMechanism = "PLAINTEXT";
    this.securityProtocol = "SASL_SSL";
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

  public String getUiRef() {
    return uiRef;
  }

  public void setUiRef(String uiRef) {
    this.uiRef = uiRef;
  }

  /**
   * Set the UI ref with a templated using a kafka ID
   *
   * <p>The template is
   * "https://cloud.redhat.com/beta/application-services/openshift-streams/kafkas/%s"
   *
   * @param kafkaId the value to replace
   */
  public void setUiRefForKafkaId(String kafkaId) {
    this.uiRef = String.format(UI_REF_TEMPLATE, kafkaId);
  }
}
