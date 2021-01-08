
package com.openshift.cloud.models.v1alpha1;

import io.dekorate.crd.annotation.CustomResource;
import io.dekorate.crd.annotation.Status;
import io.dekorate.crd.config.Scope;

@CustomResource(group = "rhoas.redhat.com", version = "v1alpha1", scope = Scope.Namespaced)
public class ManagedKafkaConnection {

    private ManagedKafkaConnectionSpec managedKafkaConnectionSpec;
    @Status
    private ManagedKafkaConnectionStatus managedKafkaConnectionStatus;


    /**
     * No args constructor for use in serialization
     * 
     */
    public ManagedKafkaConnection() {
    }

    /**
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


}
