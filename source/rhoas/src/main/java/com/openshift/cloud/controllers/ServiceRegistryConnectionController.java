package com.openshift.cloud.controllers;

import java.time.Instant;

import javax.inject.Inject;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.ServiceRegistryApiClient;
import com.openshift.cloud.utils.ConnectionResourcesMetadata;
import com.openshift.cloud.utils.InvalidUserInputException;
import com.openshift.cloud.v1alpha.models.ServiceRegistryConnection;


import io.javaoperatorsdk.operator.api.Context;

public class ServiceRegistryConnectionController extends AbstractCloudServicesController<ServiceRegistryConnection> {

    @Inject
    ServiceRegistryApiClient apiClient;

    @Inject
    AccessTokenSecretTool accessTokenSecretTool;

    @Override
    void doCreateOrUpdateResource(ServiceRegistryConnection resource, Context<ServiceRegistryConnection> context)
            throws Throwable {

        validateResource(resource);

        var registryId = resource.getSpec().getServiceRegistryId();
        var accessTokenSecretName = resource.getSpec().getAccessTokenSecretName();
        var serviceAccountSecretName = resource.getSpec().getCredentials().getServiceAccountSecretName();
        var namespace = resource.getMetadata().getNamespace();

        String accessToken = accessTokenSecretTool.getAccessToken(accessTokenSecretName, namespace);

        var registry = apiClient.getServiceRegistryById(registryId, accessToken);
        var status = resource.getStatus();
        status.setMessage("Created");
        status.setUpdated(Instant.now().toString());
        status.setRegistryUrl(registry.getRegistryUrl());
        status.setServiceAccountSecretName(serviceAccountSecretName);
        status.setMetadata(ConnectionResourcesMetadata.buildServiceMetadata());

    }

    private void validateResource(ServiceRegistryConnection resource) throws InvalidUserInputException {
        ConditionUtil.assertNotNull(resource.getSpec(), "spec");
        ConditionUtil.assertNotNull(resource.getSpec().getAccessTokenSecretName(), "spec.accessTokenSecretName");
        ConditionUtil.assertNotNull(resource.getSpec().getCredentials(), "spec.credentials");
        ConditionUtil.assertNotNull(resource.getSpec().getCredentials().getServiceAccountSecretName(),
                "spec.credentials.serviceAccountSecretName");
        ConditionUtil.assertNotNull(resource.getSpec().getServiceRegistryId(), "spec.serviceRegistryId");
        ConditionUtil.assertNotNull(resource.getMetadata().getNamespace(), "metadata.namespace");
    }

}
