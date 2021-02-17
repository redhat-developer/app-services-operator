package com.openshift.cloud.v1alpha.models;

import io.dekorate.crd.annotation.Crd;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Plural;
import io.fabric8.kubernetes.model.annotation.Version;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Plural("managedservicesrequests")
@Group("rhoas.redhat.com")
@Version("v1alpha1")
@Crd(group = "rhoas.redhat.com", version = "v1alpha1")
public class ManagedServicesRequest
    extends CustomResource<ManagedServicesRequestSpec, ManagedServicesRequestStatus>
    implements Namespaced {

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("managedServicesRequestSpec", getSpec())
        .append("managedServicesRequestStatus", getStatus())
        .toString();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(getSpec()).append(getStatus()).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof ManagedServicesRequest) == false) {
      return false;
    }
    ManagedServicesRequest rhs = ((ManagedServicesRequest) other);
    return new EqualsBuilder()
        .append(getSpec(), rhs.getSpec())
        .append(getStatus(), rhs.getStatus())
        .isEquals();
  }
}
