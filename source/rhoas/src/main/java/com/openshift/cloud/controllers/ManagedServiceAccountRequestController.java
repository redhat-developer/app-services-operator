package com.openshift.cloud.controllers;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.ManagedKafkaApiClient;
import com.openshift.cloud.beans.ManagedKafkaK8sClients;
import com.openshift.cloud.v1alpha.models.ManagedServiceAccountRequest;
import com.openshift.cloud.v1alpha.models.ManagedServiceAccountRequestStatus;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.api.config.ControllerConfiguration;
import java.time.Instant;
import java.util.logging.Logger;
import javax.inject.Inject;

/** Controller for ManagedServiceAccountRequest CRs */
@Controller(namespaces = ControllerConfiguration.WATCH_ALL_NAMESPACES_MARKER)
public class ManagedServiceAccountRequestController
    implements ResourceController<ManagedServiceAccountRequest> {

  private static final Logger LOG =
      Logger.getLogger(ManagedKafkaConnectionController.class.getName());

  @Inject AccessTokenSecretTool accessTokenSecretTool;

  @Inject ManagedKafkaK8sClients managedKafkaClientFactory;

  @Inject ManagedKafkaApiClient apiClient;

  @Override
  public DeleteControl deleteResource(
      ManagedServiceAccountRequest resource, Context<ManagedServiceAccountRequest> context) {

    return DeleteControl.DEFAULT_DELETE;
  }

  @Override
  public UpdateControl<ManagedServiceAccountRequest> createOrUpdateResource(
      ManagedServiceAccountRequest resource, Context<ManagedServiceAccountRequest> context) {

    if (resource.getStatus() != null) {
      return UpdateControl.noUpdate();
    }

    var accessTokenSecretName = resource.getSpec().getAccessTokenSecretName();
    var namespace = resource.getMetadata().getNamespace();

    var accessToken = accessTokenSecretTool.getAccessToken(accessTokenSecretName, namespace);

    var serviceAccount = apiClient.createServiceAccount(resource.getSpec(), accessToken);

    apiClient.createSecretForServiceAccount(resource, serviceAccount);

    var status =
        new ManagedServiceAccountRequestStatus(
            "Created", Instant.now().toString(), resource.getSpec().getServiceAccountSecretName());
    resource.setStatus(status);

    managedKafkaClientFactory
        .managedServiceAccountRequest()
        .inNamespace(resource.getMetadata().getNamespace())
        .updateStatus(resource);

    return UpdateControl.noUpdate();
  }
}
