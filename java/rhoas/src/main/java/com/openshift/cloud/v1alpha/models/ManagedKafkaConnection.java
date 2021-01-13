
package com.openshift.cloud.v1alpha.models;

import java.util.HashMap;
import java.util.Map;

import io.dekorate.crd.config.Scope;
import io.fabric8.kubernetes.client.CustomResource;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

//@CustomResource(group = "rhoas.redhat.com", version = "v1alpha1", scope = Scope.Namespaced)
public class ManagedKafkaConnection extends CustomResource {

    private ManagedKafkaConnectionSpec spec;
    private ManagedKafkaConnectionStatus status;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ManagedKafkaConnection() {
    }

    /**
     * 
     * @param spec
     * @param status
     */
    public ManagedKafkaConnection(ManagedKafkaConnectionSpec spec, ManagedKafkaConnectionStatus status) {
        super();
        this.spec = spec;
        this.status = status;
    }

    public ManagedKafkaConnectionSpec getSpec() {
        return spec;
    }

    public void setSpec(ManagedKafkaConnectionSpec managedKafkaConnectionSpec) {
        this.spec = managedKafkaConnectionSpec;
    }

    public ManagedKafkaConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ManagedKafkaConnectionStatus managedKafkaConnectionStatus) {
        this.status = managedKafkaConnectionStatus;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("managedKafkaConnectionSpec", spec).append("managedKafkaConnectionStatus", status).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(spec).append(status).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(spec, rhs.spec).append(status, rhs.status).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
