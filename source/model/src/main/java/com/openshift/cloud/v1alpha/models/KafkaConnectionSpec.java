package com.openshift.cloud.v1alpha.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.sundr.builder.annotations.Buildable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Buildable(builderPackage = "io.fabric8.kubernetes.api.builder", editableEnabled = false)
public class KafkaConnectionSpec {

  private String kafkaId;
  private String accessTokenSecretName;
  private Credentials credentials;

  /** No args constructor for use in serialization */
  public KafkaConnectionSpec() {}

  public String getAccessTokenSecretName() {
    return accessTokenSecretName;
  }

  public void setAccessTokenSecretName(String accessTokenSecretName) {
    this.accessTokenSecretName = accessTokenSecretName;
  }

  /**
   * @param kafkaId
   * @param credentials
   */
  public KafkaConnectionSpec(String kafkaId, Credentials credentials) {
    super();
    this.kafkaId = kafkaId;
    this.credentials = credentials;
  }

  public String getKafkaId() {
    return kafkaId;
  }

  public void setKafkaId(String kafkaId) {
    this.kafkaId = kafkaId;
  }

  public Credentials getCredentials() {
    return credentials;
  }

  public void setCredentials(Credentials credentials) {
    this.credentials = credentials;
  }
}
