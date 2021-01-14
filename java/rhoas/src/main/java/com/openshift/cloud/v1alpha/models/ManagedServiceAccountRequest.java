
package com.openshift.cloud.v1alpha.models;

import java.util.HashMap;
import java.util.Map;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

//@CustomResource(group = "rhoas.redhat.com", version = "v1alpha1", scope = Scope.Namespaced)
public class ManagedServiceAccountRequest {

    private ManagedServiceAccountRequestSpec managedServiceAccountRequestSpec;
    private ManagedServiceAccountRequestStatus managedServiceAccountRequestStatus;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public ManagedServiceAccountRequest() {
    }

    /**
     * 
     * @param managedServiceAccountRequestSpec
     * @param managedServiceAccountRequestStatus
     */
    public ManagedServiceAccountRequest(ManagedServiceAccountRequestSpec managedServiceAccountRequestSpec, ManagedServiceAccountRequestStatus managedServiceAccountRequestStatus) {
        super();
        this.managedServiceAccountRequestSpec = managedServiceAccountRequestSpec;
        this.managedServiceAccountRequestStatus = managedServiceAccountRequestStatus;
    }

    public ManagedServiceAccountRequestSpec getManagedServiceAccountRequestSpec() {
        return managedServiceAccountRequestSpec;
    }

    public void setManagedServiceAccountRequestSpec(ManagedServiceAccountRequestSpec managedServiceAccountRequestSpec) {
        this.managedServiceAccountRequestSpec = managedServiceAccountRequestSpec;
    }

    public ManagedServiceAccountRequestStatus getManagedServiceAccountRequestStatus() {
        return managedServiceAccountRequestStatus;
    }

    public void setManagedServiceAccountRequestStatus(ManagedServiceAccountRequestStatus managedServiceAccountRequestStatus) {
        this.managedServiceAccountRequestStatus = managedServiceAccountRequestStatus;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("managedServiceAccountRequestSpec", managedServiceAccountRequestSpec).append("managedServiceAccountRequestStatus", managedServiceAccountRequestStatus).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(managedServiceAccountRequestSpec).append(additionalProperties).append(managedServiceAccountRequestStatus).toHashCode();
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
        return new EqualsBuilder().append(managedServiceAccountRequestSpec, rhs.managedServiceAccountRequestSpec).append(additionalProperties, rhs.additionalProperties).append(managedServiceAccountRequestStatus, rhs.managedServiceAccountRequestStatus).isEquals();
    }

}
