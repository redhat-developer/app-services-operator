package com.openshift.cloud.controllers;

import java.time.Instant;

import javax.inject.Inject;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.ServiceRegistryApiClient;
import com.openshift.cloud.utils.ConnectionResourcesMetadata;
import com.openshift.cloud.utils.InvalidUserInputException;
import com.openshift.cloud.v1alpha.models.ServiceRegistryConnection;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.javaoperatorsdk.operator.api.Context;

public class ServiceRegistryConnectionController
    extends AbstractCloudServicesController<ServiceRegistryConnection> {

  @Inject
  ServiceRegistryApiClient apiClient;

  @Inject
  AccessTokenSecretTool accessTokenSecretTool;

  @ConfigProperty(name = "rhoas.client.srsOAuthHost",
      defaultValue = "https://identity.api.stage.openshift.com/auth/realms/rhoas")
  String oAuthHost;

  @Override
  void doCreateOrUpdateResource(ServiceRegistryConnection resource,
      Context<ServiceRegistryConnection> context) throws Throwable {

    validateResource(resource);

    var registryId = resource.getSpec().getServiceRegistryId();
    var accessTokenSecretName = resource.getSpec().getAccessTokenSecretName();
    var serviceAccountSecretName =
        resource.getSpec().getCredentials().getServiceAccountSecretName();
    var namespace = resource.getMetadata().getNamespace();

    String accessToken = accessTokenSecretTool.getAccessToken(accessTokenSecretName, namespace);

    var registry = apiClient.getServiceRegistryById(registryId, accessToken);
    var status = resource.getStatus();
    status.setMessage("Created");
    status.setUpdated(Instant.now().toString());
    status.setRegistryUrl(registry.getRegistryUrl());
    status.setDirection(resource.getSpec().getDirection());
    status.setServiceAccountSecretName(serviceAccountSecretName);
    status.setMetadata(ConnectionResourcesMetadata.buildServiceMetadata(oAuthHost));

  }

  private void validateResource(ServiceRegistryConnection resource)
      throws InvalidUserInputException {
    ConditionUtil.assertNotNull(resource.getSpec(), "spec");
    ConditionUtil.assertNotNull(resource.getSpec().getAccessTokenSecretName(),
        "spec.accessTokenSecretName");
    ConditionUtil.assertNotNull(resource.getSpec().getCredentials(), "spec.credentials");
    ConditionUtil.assertNotNull(resource.getSpec().getCredentials().getServiceAccountSecretName(),
        "spec.credentials.serviceAccountSecretName");
    ConditionUtil.assertNotNull(resource.getSpec().getServiceRegistryId(),
        "spec.serviceRegistryId");
    ConditionUtil.assertNotNull(resource.getMetadata().getNamespace(), "metadata.namespace");
  }

}
