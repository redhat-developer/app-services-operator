
package com.openshift.cloud.models.v1alpha1;

import io.dekorate.crd.annotation.CustomResource;
import io.dekorate.crd.confg.Scope;

@CustomResource(group = "rhoas.redhat.com", version = "v1alpha1", scope = Scope.Namespaced)
public class ManagedKafkaRequest {

    private ManagedKafkaRequestSpec managedKafkaRequestSpec;
    private ManagedKafkaRequestStatus managedKafkaRequestStatus;

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
}
