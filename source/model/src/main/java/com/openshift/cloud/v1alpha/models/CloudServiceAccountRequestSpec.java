package com.openshift.cloud.v1alpha.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.sundr.builder.annotations.Buildable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Buildable(builderPackage = "io.fabric8.kubernetes.api.builder", editableEnabled = false)
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

}
