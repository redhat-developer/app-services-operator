package com.openshift.cloud.v1alpha.models;
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
public class Credentials {

  private String serviceAccountSecretName;

  /** @param serviceAccountSecretName */
  public Credentials(String serviceAccountSecretName) {
    super();
    this.serviceAccountSecretName = serviceAccountSecretName;
  }

 
}
