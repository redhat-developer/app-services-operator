package com.openshift.cloud.controllers;

import com.openshift.cloud.v1alpha.models.ManagedKafkaConnection;
import com.openshift.cloud.v1alpha.models.ManagedKafkaRequest;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;

import java.util.logging.Logger;

@Controller
public class ManagedKafkaRequestController  implements ResourceController<ManagedKafkaRequest> {

    private static final Logger LOG = Logger.getLogger(ManagedKafkaRequestController.class.getName());

    @Override
    public DeleteControl deleteResource(ManagedKafkaRequest managedKafkaRequest, Context<ManagedKafkaRequest> context) {
        LOG.info(String.format("Deleting resource %s", managedKafkaRequest.getMetadata().getName()));

        return DeleteControl.DEFAULT_DELETE;
    }

    @Override
    public UpdateControl<ManagedKafkaRequest> createOrUpdateResource(ManagedKafkaRequest managedKafkaRequest, Context<ManagedKafkaRequest> context) {
        LOG.info(String.format("Update or create resource %s", managedKafkaRequest.getMetadata().getName()));

        return UpdateControl.noUpdate();
    }

    @Override
    public void init(EventSourceManager eventSourceManager) {
        LOG.info("Init! This is where we would add watches for child resources");
        
    }
}
