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

  /** No args constructor for use in serialization */
  public UserKafka() {}

  /**
   * @param owner
   * @param provider
   * @param created
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getBootstrapServerHost() {
    return bootstrapServerHost;
  }

  public void setBootstrapServerHost(String bootstrapServerHost) {
    this.bootstrapServerHost = bootstrapServerHost;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserKafka userKafka = (UserKafka) o;

    return id.equals(userKafka.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getStatus() {
    return status;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
