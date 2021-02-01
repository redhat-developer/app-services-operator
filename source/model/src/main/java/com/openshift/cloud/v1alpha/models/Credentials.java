package com.openshift.cloud.v1alpha.models;

public class Credentials {

  private String serviceAccountSecretName;

  /** No args constructor for use in serialization */
  public Credentials() {}

  /** @param serviceAccountSecretName */
  public Credentials(String serviceAccountSecretName) {
    super();
    this.serviceAccountSecretName = serviceAccountSecretName;
  }

  public String getServiceAccountSecretName() {
    return serviceAccountSecretName;
  }

  public void setServiceAccountSecretName(String serviceAccountSecretName) {
    this.serviceAccountSecretName = serviceAccountSecretName;
  }
}
