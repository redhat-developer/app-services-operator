package com.openshift.cloud.controllers;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.ServiceRegistryApiClient;
import com.openshift.cloud.utils.ConnectionResourcesMetadata;
import com.openshift.cloud.utils.InvalidUserInputException;
import com.openshift.cloud.v1alpha.models.ServiceRegistryConnection;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import java.time.Instant;

@Controller(finalizerName = Controller.NO_FINALIZER)
public class ServiceRegistryConnectionController
    extends AbstractCloudServicesController<ServiceRegistryConnection> {

  @Inject
  ServiceRegistryApiClient apiClient;

  @Inject
  AccessTokenSecretTool accessTokenSecretTool;

  @ConfigProperty(name = "rhoas.client.srsOAuthHost",
      defaultValue = "https://identity.api.openshift.com/auth")
  String oAuthHost;

  @ConfigProperty(name = "rhoas.client.srsOAuthRealm", defaultValue = "rhoas")
  String oAuthRealm;


  @ConfigProperty(name = "rhoas.client.srsOAuthTokenPath",
      defaultValue = "protocol/openid-connect/token")
  String oAuthTokenPath;

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
    status.setServiceAccountSecretName(serviceAccountSecretName);
    status.setMetadata(
        ConnectionResourcesMetadata.buildServiceMetadata(oAuthHost, oAuthRealm, oAuthTokenPath));

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
