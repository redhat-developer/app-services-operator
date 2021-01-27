package com.openshift.cloud.v1alpha.models;

import io.dekorate.crd.annotation.Crd;
import io.dekorate.crd.annotation.Status;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Plural;
import io.fabric8.kubernetes.model.annotation.Version;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Plural("managedserviceaccountrequests")
@Group("rhoas.redhat.com")
@Version("v1alpha1")
@Crd(group = "rhoas.redhat.com", version = "v1alpha1")
public class ManagedServiceAccountRequest
    extends CustomResource<ManagedServiceAccountRequestSpec, ManagedServiceAccountRequestStatus> {

  @Status private ManagedServiceAccountRequestStatus status;

  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("managedServiceAccountRequestSpec", getSpec())
        .append("managedServiceAccountRequestStatus", getStatus())
        .append("additionalProperties", additionalProperties)
        .toString();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getSpec())
        .append(additionalProperties)
        .append(getStatus())
        .toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof ManagedServiceAccountRequest) == false) {
      return false;
    }
    ManagedServiceAccountRequest rhs = ((ManagedServiceAccountRequest) other);
    return new EqualsBuilder()
        .append(getSpec(), rhs.getSpec())
        .append(additionalProperties, rhs.additionalProperties)
        .append(getStatus(), rhs.getStatus())
        .isEquals();
  }

  @Override
  public ManagedServiceAccountRequestStatus getStatus() {
    return this.status;
  }

  @Override
  public void setStatus(ManagedServiceAccountRequestStatus status) {
    this.status = status;
  }
}
