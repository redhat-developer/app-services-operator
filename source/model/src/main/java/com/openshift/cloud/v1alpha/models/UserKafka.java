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

  public UserKafka setId(String id) {
    this.id = id;
    return this;
  }

  public String getProvider() {
    return provider;
  }

  public UserKafka setProvider(String provider) {
    this.provider = provider;
    return this;
  }

  public String getRegion() {
    return region;
  }

  public UserKafka setRegion(String region) {
    this.region = region;
    return this;
  }

  public String getOwner() {
    return owner;
  }

  public UserKafka setOwner(String owner) {
    this.owner = owner;
    return this;
  }

  public String getBootstrapServerHost() {
    return bootstrapServerHost;
  }

  public UserKafka setBootstrapServerHost(String bootstrapServerHost) {
    this.bootstrapServerHost = bootstrapServerHost;
    return this;
  }

  public UserKafka setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public UserKafka setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public UserKafka setStatus(String status) {
    this.status = status;
    return this;
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

  public UserKafka setName(String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
  }
}
