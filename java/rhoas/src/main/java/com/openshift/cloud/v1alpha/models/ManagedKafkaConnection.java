
package com.openshift.cloud.v1alpha.models;

import java.util.HashMap;
import java.util.Map;

import io.dekorate.crd.annotation.CustomResource;
import io.dekorate.crd.config.Scope;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@CustomResource(group = "rhoas.redhat.com", version = "v1alpha1", scope = Scope.Namespaced)
public class ManagedKafkaConnection {

    private ManagedKafkaConnectionSpec managedKafkaConnectionSpec;
    private ManagedKafkaConnectionStatus managedKafkaConnectionStatus;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ManagedKafkaConnection() {
    }

    /**
     * 
     * @param managedKafkaConnectionSpec
     * @param managedKafkaConnectionStatus
     */
    public ManagedKafkaConnection(ManagedKafkaConnectionSpec managedKafkaConnectionSpec, ManagedKafkaConnectionStatus managedKafkaConnectionStatus) {
        super();
        this.managedKafkaConnectionSpec = managedKafkaConnectionSpec;
        this.managedKafkaConnectionStatus = managedKafkaConnectionStatus;
    }

    public ManagedKafkaConnectionSpec getManagedKafkaConnectionSpec() {
        return managedKafkaConnectionSpec;
    }

    public void setManagedKafkaConnectionSpec(ManagedKafkaConnectionSpec managedKafkaConnectionSpec) {
        this.managedKafkaConnectionSpec = managedKafkaConnectionSpec;
    }

    public ManagedKafkaConnectionStatus getManagedKafkaConnectionStatus() {
        return managedKafkaConnectionStatus;
    }

    public void setManagedKafkaConnectionStatus(ManagedKafkaConnectionStatus managedKafkaConnectionStatus) {
        this.managedKafkaConnectionStatus = managedKafkaConnectionStatus;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("managedKafkaConnectionSpec", managedKafkaConnectionSpec).append("managedKafkaConnectionStatus", managedKafkaConnectionStatus).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(managedKafkaConnectionSpec).append(managedKafkaConnectionStatus).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(managedKafkaConnectionSpec, rhs.managedKafkaConnectionSpec).append(managedKafkaConnectionStatus, rhs.managedKafkaConnectionStatus).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
