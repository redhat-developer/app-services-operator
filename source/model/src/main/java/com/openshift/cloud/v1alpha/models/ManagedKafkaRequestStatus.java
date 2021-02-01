package com.openshift.cloud.v1alpha.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagedKafkaRequestStatus {

  private List<UserKafka> userKafkas = null;
  private String lastUpdate;

  /** No args constructor for use in serialization */
  public ManagedKafkaRequestStatus() {}

  /**
   * @param userKafkas
   * @param lastUpdate
   */
  public ManagedKafkaRequestStatus(String lastUpdate, List<UserKafka> userKafkas) {
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
}
