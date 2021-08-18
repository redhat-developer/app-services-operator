package com.openshift.cloud.v1alpha.models;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Plural;
import io.fabric8.kubernetes.model.annotation.Version;
import io.sundr.builder.annotations.Buildable;
import io.sundr.builder.annotations.BuildableReference;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Plural("kafkaconnections")
@Group("rhoas.redhat.com")
@Version("v1alpha1")
@Buildable(builderPackage = "io.fabric8.kubernetes.api.builder", editableEnabled = false,
    refs = @BuildableReference(CustomResource.class))
public class KafkaConnection extends CustomResource<KafkaConnectionSpec, KafkaConnectionStatus>
    implements Namespaced {

  public static final String COMPONENT_LABEL_KEY = "app.kubernetes.io/component";
  public static final String MANAGED_BY_LABEL_KEY = "app.kubernetes.io/managed-by";
    
  public  static final String COMPONENT_LABEL_VALUE = "external-service";
  public static final String MANAGED_BY_LABEL_VALUE = "rhoas";

  /** */
  private static final long serialVersionUID = 7721054567486507997L;

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("kafkaConnectionSpec", getSpec())
        .append("kafkaConnectionStatus", getStatus()).toString();
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
    if ((other instanceof KafkaConnection) == false) {
      return false;
    }
    KafkaConnection rhs = ((KafkaConnection) other);
    return new EqualsBuilder().append(getSpec(), rhs.getSpec()).append(getStatus(), rhs.getStatus())
        .isEquals();
  }

  @Override
  public KafkaConnectionSpec getSpec() {
    return super.getSpec();
  }

  @Override
  public void setSpec(KafkaConnectionSpec spec) {
    super.setSpec(spec);
  }

  @Override
  public KafkaConnectionStatus getStatus() {
    return super.getStatus();
  }

  @Override
  public void setStatus(KafkaConnectionStatus status) {
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
