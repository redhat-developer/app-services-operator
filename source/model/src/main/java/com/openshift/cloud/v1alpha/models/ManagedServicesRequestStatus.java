package com.openshift.cloud.v1alpha.models;

import io.sundr.builder.annotations.Buildable;
import java.util.List;

@Buildable
public class CloudServicesRequestStatus {

  private List<UserKafka> userKafkas;
  private String lastUpdate;
  private List<ManagedKafkaCondition> conditions;

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

  public List<ManagedKafkaCondition> getConditions() {
    return conditions;
  }

  public void setConditions(List<ManagedKafkaCondition> conditions) {
    this.conditions = conditions;
  }
}
