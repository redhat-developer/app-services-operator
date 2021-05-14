package com.openshift.cloud.v1alpha.models;

public class SchemaRegistry {
  private Integer id;
  private String registryStatus;
  private String registryUrl;
  private String name;
  private String lastUpdated;
  private Integer registryDeploymentId;

  public SchemaRegistry() {}

  public SchemaRegistry(Integer id, String registryStatus, String registryUrl, String name,
      String lastUpdated, Integer registryDeploymentId) {
    this.id = id;
    this.registryStatus = registryStatus;
    this.registryUrl = registryUrl;
    this.name = name;
    this.lastUpdated = lastUpdated;
    this.registryDeploymentId = registryDeploymentId;
  }

  public Integer getId() {
    return id;
  }

  public SchemaRegistry setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getRegistryStatus() {
    return registryStatus;
  }

  public SchemaRegistry setRegistryStatus(String registryStatus) {
    this.registryStatus = registryStatus;
    return this;
  }

  public String getRegistryUrl() {
    return registryUrl;
  }

  public SchemaRegistry setRegistryUrl(String registryUrl) {
    this.registryUrl = registryUrl;
    return this;
  }

  public String getName() {
    return name;
  }

  public SchemaRegistry setName(String name) {
    this.name = name;
    return this;
  }

  public String getLastUpdated() {
    return lastUpdated;
  }

  public SchemaRegistry setLastUpdated(String lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  public Integer getRegistryDeploymentId() {
    return registryDeploymentId;
  }

  public SchemaRegistry setRegistryDeploymentId(Integer registryDeploymentId) {
    this.registryDeploymentId = registryDeploymentId;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    SchemaRegistry that = (SchemaRegistry) o;

    if (id != null ? !id.equals(that.id) : that.id != null)
      return false;
    if (registryStatus != null ? !registryStatus.equals(that.registryStatus)
        : that.registryStatus != null)
      return false;
    if (registryUrl != null ? !registryUrl.equals(that.registryUrl) : that.registryUrl != null)
      return false;
    if (name != null ? !name.equals(that.name) : that.name != null)
      return false;
    if (lastUpdated != null ? !lastUpdated.equals(that.lastUpdated) : that.lastUpdated != null)
      return false;
    return registryDeploymentId != null ? registryDeploymentId.equals(that.registryDeploymentId)
        : that.registryDeploymentId == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (registryStatus != null ? registryStatus.hashCode() : 0);
    result = 31 * result + (registryUrl != null ? registryUrl.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (lastUpdated != null ? lastUpdated.hashCode() : 0);
    result = 31 * result + (registryDeploymentId != null ? registryDeploymentId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SchemaRegistry{");
    sb.append("id=").append(id);
    sb.append(", registryStatus='").append(registryStatus).append('\'');
    sb.append(", registryUrl='").append(registryUrl).append('\'');
    sb.append(", name='").append(name).append('\'');
    sb.append(", lastUpdated='").append(lastUpdated).append('\'');
    sb.append(", registryDeploymentId=").append(registryDeploymentId);
    sb.append('}');
    return sb.toString();
  }
}
