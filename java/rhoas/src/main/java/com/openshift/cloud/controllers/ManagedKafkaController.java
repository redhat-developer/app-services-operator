package com.openshift.cloud.controllers;

import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.DeleteControl;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;

public class ManagedKafkaController implements ResourceController {
    private KubernetesClient client;

    public ManagedKafkaController(KubernetesClient client) {

        this.client = client;
    }

    /**
     * The implementation should delete the associated component(s). Note that this is method is
     * called when an object is marked for deletion. After its executed the custom resource finalizer
     * is automatically removed by the framework; unless the return value is false - note that this is
     * almost never the case. Its important to have the implementation also idempotent, in the current
     * implementation to cover all edge cases actually will be executed mostly twice.
     *
     * @param resource
     * @param context
     * @return true - so the finalizer is automatically removed after the call. false if you don't
     * want to remove the finalizer. Note that this is ALMOST NEVER the case.
     */
    @Override
    public DeleteControl deleteResource(CustomResource resource, Context context) {
        return null;
    }

    /**
     * The implementation of this operation is required to be idempotent. Always use the UpdateControl
     * object to make updates on custom resource if possible. Also always use the custom resource
     * parameter (not the custom resource that might be in the events)
     *
     * @param resource
     * @param context
     * @return The resource is updated in api server if the return value is present within Optional.
     * This the common use cases. However in cases, for example the operator is restarted, and we
     * don't want to have an update call to k8s api to be made unnecessarily, by returning an
     * empty Optional this update can be skipped. <b>However we will always call an update if
     * there is no finalizer on object and its not marked for deletion.</b>
     */
    @Override
    public UpdateControl createOrUpdateResource(CustomResource resource, Context context) {
        return null;
    }

    /**
     * In init typically you might want to register event sources.
     *
     * @param eventSourceManager
     */
    @Override
    public void init(EventSourceManager eventSourceManager) {

    }

    @Override
    public String getName() {
        return null;
    }
}
