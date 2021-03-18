package com.openshift.cloud.v1alpha.models;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Plural;
import io.fabric8.kubernetes.model.annotation.Version;
import io.sundr.builder.annotations.Buildable;
import io.sundr.builder.annotations.BuildableReference;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Plural("cloudservicesrequests")
@Group("rhoas.redhat.com")
@Version("v1alpha1")
@Buildable(
    builderPackage = "io.fabric8.kubernetes.api.builder",
    editableEnabled = false,
    refs = @BuildableReference(CustomResource.class))
public class CloudServicesRequest
    extends CustomResource<CloudServicesRequestSpec, CloudServicesRequestStatus>
    implements Namespaced {

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("cloudServicesRequestSpec", getSpec())
        .append("cloudServicesRequestStatus", getStatus())
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
    if ((other instanceof CloudServicesRequest) == false) {
      return false;
    }
    CloudServicesRequest rhs = ((CloudServicesRequest) other);
    return new EqualsBuilder()
        .append(getSpec(), rhs.getSpec())
        .append(getStatus(), rhs.getStatus())
        .isEquals();
  }
}
