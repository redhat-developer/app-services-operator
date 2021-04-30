package com.openshift.cloud.v1alpha.models;

import io.sundr.builder.annotations.Buildable;
import java.util.ArrayList;
import java.util.List;
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
@Buildable(builderPackage = "io.fabric8.kubernetes.api.builder", editableEnabled = false)
public class CloudServiceAccountRequestStatus {

  private String message = "";
  private String updated = "";
  private String serviceAccountSecretName = "";
  private List<KafkaCondition> conditions = new ArrayList<>();


  /**
   * @param serviceAccountSecretName
   * @param message
   * @param updated
   */
  public CloudServiceAccountRequestStatus(
      String message, String updated, String serviceAccountSecretName) {
    super();
    this.message = message;
    this.updated = updated;
    this.serviceAccountSecretName = serviceAccountSecretName;
  }

}
