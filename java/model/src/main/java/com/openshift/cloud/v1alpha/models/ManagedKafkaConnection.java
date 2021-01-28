package com.openshift.cloud.v1alpha.models;

import io.dekorate.crd.annotation.Crd;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Plural;
import io.fabric8.kubernetes.model.annotation.Version;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Plural("managedkafkaconnections")
@Group("rhoas.redhat.com")
@Version("v1alpha1")
@Crd(group = "rhoas.redhat.com", version = "v1alpha1")
public class ManagedKafkaConnection
    extends CustomResource<ManagedKafkaConnectionSpec, ManagedKafkaConnectionStatus>
    implements Namespaced {

  /** */
  private static final long serialVersionUID = 7721054567486507997L;

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
        .append("managedKafkaConnectionSpec", getSpec())
        .append("managedKafkaConnectionStatus", getStatus())
        .append("additionalProperties", additionalProperties)
        .toString();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getSpec())
        .append(getStatus())
        .append(additionalProperties)
        .toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof ManagedKafkaConnection) == false) {
      return false;
    }
    ManagedKafkaConnection rhs = ((ManagedKafkaConnection) other);
    return new EqualsBuilder()
        .append(getSpec(), rhs.getSpec())
        .append(getStatus(), rhs.getStatus())
        .append(additionalProperties, rhs.additionalProperties)
        .isEquals();
  }

  @Override
  public ManagedKafkaConnectionSpec getSpec() {
    return super.getSpec();
  }

  @Override
  public void setSpec(ManagedKafkaConnectionSpec spec) {
    super.setSpec(spec);
  }

  @Override
  public ManagedKafkaConnectionStatus getStatus() {
    return super.getStatus();
  }

  @Override
  public void setStatus(ManagedKafkaConnectionStatus status) {
    super.setStatus(status);
  }

  @Override
  public ObjectMeta getMetadata() {
    return super.getMetadata();
  }

  @Override
  public void setMetadata(ObjectMeta metadata) {
    super.setMetadata(metadata);
  }
}
