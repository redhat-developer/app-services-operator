package com.openshift.cloud.v1alpha.models;

public class UserKafka {

  private String id;
  private String provider;
  private String region;
  private String owner;
  private String bootstrapServerHost;
  private String updatedAt;
  private String createdAt;
  private String status;
  private String name;

  /**
   * @param owner
   * @param provider
   * @param id
   * @param region
   */
  public UserKafka(String id, String provider, String region, String owner) {
    super();
    this.id = id;
    this.provider = provider;
    this.region = region;
    this.owner = owner;
  }

}
