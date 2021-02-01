package com.openshift.cloud.v1alpha.models;

public class ManagedKafkaConnectionSpec {

  private String kafkaId;
  private String accessTokenSecretName;
  private Credentials credentials;

  /** No args constructor for use in serialization */
  public ManagedKafkaConnectionSpec() {}

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
  public ManagedKafkaConnectionSpec(String kafkaId, Credentials credentials) {
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
