package com.openshift.cloud.v1alpha.models;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ManagedServicesRequestSpec {

  private String accessTokenSecretName = "rhoas_binding_operator_token"; // Default as per ADR_00022

  /** No args constructor for use in serialization */
  public ManagedServicesRequestSpec() {}

  /** @param accessTokenSecretName */
  public ManagedServicesRequestSpec(String accessTokenSecretName) {
    super();
    this.accessTokenSecretName = accessTokenSecretName;
  }

  public String getAccessTokenSecretName() {
    return accessTokenSecretName;
  }

  public void setAccessTokenSecretName(String accessTokenSecretName) {
    this.accessTokenSecretName = accessTokenSecretName;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("accessTokenSecretName", accessTokenSecretName)
        .toString();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(accessTokenSecretName).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof ManagedServicesRequestSpec) == false) {
      return false;
    }
    ManagedServicesRequestSpec rhs = ((ManagedServicesRequestSpec) other);
    return new EqualsBuilder().append(accessTokenSecretName, rhs.accessTokenSecretName).isEquals();
  }
}
