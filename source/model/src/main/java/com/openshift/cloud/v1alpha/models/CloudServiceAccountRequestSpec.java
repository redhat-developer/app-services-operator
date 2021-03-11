package com.openshift.cloud.v1alpha.models;

public class CloudServiceAccountRequestSpec {

  private String serviceAccountName;
  private String serviceAccountDescription;
  private String serviceAccountSecretName;
  private String accessTokenSecretName = "rhoas_binding_operator_token";

  /** No args constructor for use in serialization */
  public CloudServiceAccountRequestSpec() {}

  public String getAccessTokenSecretName() {
    return accessTokenSecretName;
  }

  public void setAccessTokenSecretName(String accessTokenSecretName) {
    this.accessTokenSecretName = accessTokenSecretName;
  }

  /**
   * @param serviceAccountSecretName
   * @param serviceAccountName
   * @param serviceAccountDescription
   */
  public CloudServiceAccountRequestSpec(
      String serviceAccountName,
      String serviceAccountDescription,
      String serviceAccountSecretName) {
    super();
    this.serviceAccountName = serviceAccountName;
    this.serviceAccountDescription = serviceAccountDescription;
    this.serviceAccountSecretName = serviceAccountSecretName;
  }

  public String getServiceAccountName() {
    return serviceAccountName;
  }

  public void setServiceAccountName(String serviceAccountName) {
    this.serviceAccountName = serviceAccountName;
  }

  public String getServiceAccountDescription() {
    return serviceAccountDescription;
  }

  public void setServiceAccountDescription(String serviceAccountDescription) {
    this.serviceAccountDescription = serviceAccountDescription;
  }

  public String getServiceAccountSecretName() {
    return serviceAccountSecretName;
  }

  public void setServiceAccountSecretName(String serviceAccountSecretName) {
    this.serviceAccountSecretName = serviceAccountSecretName;
  }
}
