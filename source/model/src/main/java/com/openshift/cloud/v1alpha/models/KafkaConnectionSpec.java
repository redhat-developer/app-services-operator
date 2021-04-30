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
public class KafkaConnectionSpec {

  private String kafkaId;
  private String accessTokenSecretName;
  private Credentials credentials;

  

  public String getAccessTokenSecretName() {
    return accessTokenSecretName;
  }

  
}
