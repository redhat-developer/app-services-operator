package com.openshift.cloud.controllers;

import java.util.logging.Logger;

import com.openshift.cloud.v1alpha.models.ManagedKafkaConnection;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.DeleteControl;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;

@Controller(crdName = "managedkafkaconnections.rhoas.redhat.com")
public class ManagedKafkaConnectionController implements ResourceController<ManagedKafkaConnection> {

    private static final Logger LOG = Logger.getLogger(ManagedKafkaConnectionController.class.getName());

    private KubernetesClient client;

    public ManagedKafkaConnectionController(KubernetesClient client) {

        this.client = client;
    }

    @Override
    public DeleteControl deleteResource(ManagedKafkaConnection resource, Context<ManagedKafkaConnection> context) {
        LOG.info(String.format("Deleting resource %s", resource.getMetadata().getName()));
        // client.
        //     .services()
        //     .inNamespace(resource.getMetadata().getNamespace())
        //     .withName(resource.getSpec().getName())
        //     .delete();
        return DeleteControl.DEFAULT_DELETE;
    }

    @Override
    public UpdateControl<ManagedKafkaConnection> createOrUpdateResource(ManagedKafkaConnection resource, Context<ManagedKafkaConnection> context) {
        LOG.info(String.format("Creating or Updating resource %s", resource.getMetadata().getName()));
        // client.
        //     .services()
        //     .inNamespace(resource.getMetadata().getNamespace())
        //     .withName(resource.getSpec().getName())
        //     .delete();
        return UpdateControl.noUpdate();
    }

    /**
     * In init typically you might want to register event sources.
     *
     * @param eventSourceManager
     */
    @Override
    public void init(EventSourceManager eventSourceManager) {
        LOG.info("Init! This is where we would add watches for child resources");
    }

    // @Override
    // public String getName() {
    //     return ManagedKafkaConnectionController.class.getName();
    // }
}
