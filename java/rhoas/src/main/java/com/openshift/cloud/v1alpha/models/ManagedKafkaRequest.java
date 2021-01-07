
package com.openshift.cloud.v1alpha.models;

import java.util.HashMap;
import java.util.Map;
import io.dekorate.crd.annotation.CustomResource;
import io.dekorate.crd.annotation.Status;
import io.dekorate.crd.config.Scope;

@CustomResource(group = "rhoas.redhat.com", version = "v1alpha1", scope = Scope.Namespaced)
public class ManagedKafkaRequest {

    private ManagedKafkaRequestSpec managedKafkaRequestSpec;
    private ManagedKafkaRequestStatus managedKafkaRequestStatus;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ManagedKafkaRequest() {
    }

    /**
     * 
     * @param managedKafkaRequestSpec
     * @param managedKafkaRequestStatus
     */
    public ManagedKafkaRequest(ManagedKafkaRequestSpec managedKafkaRequestSpec, ManagedKafkaRequestStatus managedKafkaRequestStatus) {
        super();
        this.managedKafkaRequestSpec = managedKafkaRequestSpec;
        this.managedKafkaRequestStatus = managedKafkaRequestStatus;
    }

    public ManagedKafkaRequestSpec getManagedKafkaRequestSpec() {
        return managedKafkaRequestSpec;
    }

    public void setManagedKafkaRequestSpec(ManagedKafkaRequestSpec managedKafkaRequestSpec) {
        this.managedKafkaRequestSpec = managedKafkaRequestSpec;
    }

    public ManagedKafkaRequestStatus getManagedKafkaRequestStatus() {
        return managedKafkaRequestStatus;
    }

    public void setManagedKafkaRequestStatus(ManagedKafkaRequestStatus managedKafkaRequestStatus) {
        this.managedKafkaRequestStatus = managedKafkaRequestStatus;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("managedKafkaRequestSpec", managedKafkaRequestSpec).append("managedKafkaRequestStatus", managedKafkaRequestStatus).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(managedKafkaRequestSpec).append(additionalProperties).append(managedKafkaRequestStatus).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ManagedKafkaRequest) == false) {
            return false;
        }
        ManagedKafkaRequest rhs = ((ManagedKafkaRequest) other);
        return new EqualsBuilder().append(managedKafkaRequestSpec, rhs.managedKafkaRequestSpec).append(additionalProperties, rhs.additionalProperties).append(managedKafkaRequestStatus, rhs.managedKafkaRequestStatus).isEquals();
    }

}
