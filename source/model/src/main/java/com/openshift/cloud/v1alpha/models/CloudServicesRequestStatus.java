package com.openshift.cloud.v1alpha.models;

import io.sundr.builder.annotations.Buildable;
import java.util.List;

@Buildable(builderPackage = "io.fabric8.kubernetes.api.builder", editableEnabled = false)
public class CloudServicesRequestStatus {

  private List<UserKafka> userKafkas;
  private List<SchemaRegistry> schemaRegistries;
  private String lastUpdate;
  private List<KafkaCondition> conditions;

  /** No args constructor for use in serialization */
  public CloudServicesRequestStatus() {}

  /**
   * @param userKafkas
   * @param lastUpdate
   */
  public CloudServicesRequestStatus(String lastUpdate, List<UserKafka> userKafkas) {
    super();
    this.lastUpdate = lastUpdate;
    this.userKafkas = userKafkas;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(String lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public List<UserKafka> getUserKafkas() {
    return userKafkas;
  }

  public void setUserKafkas(List<UserKafka> userKafkas) {
    this.userKafkas = userKafkas;
  }

  public List<SchemaRegistry> getSchemaRegistries() {
    return schemaRegistries;
  }

  public void setSchemaRegistries(List<SchemaRegistry> schemaRegistries) {
    this.schemaRegistries = schemaRegistries;
  }

  public List<KafkaCondition> getConditions() {
    return conditions;
  }

  public void setConditions(List<KafkaCondition> conditions) {
    this.conditions = conditions;
  }
}
