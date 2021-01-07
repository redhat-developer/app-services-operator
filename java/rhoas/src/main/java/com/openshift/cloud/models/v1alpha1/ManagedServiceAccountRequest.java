
package com.openshift.cloud.models.v1alpha1;

import io.dekorate.crd.annotation.CustomResource;
import io.dekorate.crd.confg.Scope;

@CustomResource(group = "rhoas.redhat.com", version = "v1alpha1", scope = Scope.Namespaced)
public class ManagedServiceAccountRequest {

    private ManagedServiceAccountRequestSpec managedServiceAccountRequestSpec;
    private ManagedServiceAccountRequestStatus managedServiceAccountRequestStatus;

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

}
