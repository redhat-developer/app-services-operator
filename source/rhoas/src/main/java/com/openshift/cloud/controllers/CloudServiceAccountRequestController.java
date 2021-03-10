package com.openshift.cloud.controllers;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.KafkaApiClient;
import com.openshift.cloud.beans.KafkaK8sClients;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequest;
import io.javaoperatorsdk.operator.api.*;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/** Controller for CloudServiceAccountRequest CRs */
@Controller
public class CloudServiceAccountRequestController
    implements ResourceController<CloudServiceAccountRequest> {

  private static final Logger LOG =
      Logger.getLogger(KafkaConnectionController.class.getName());

  @Inject AccessTokenSecretTool accessTokenSecretTool;

  @Inject KafkaK8sClients kafkaClientFactory;

  @Inject KafkaApiClient apiClient;

  @Override
  public DeleteControl deleteResource(
      CloudServiceAccountRequest resource, Context<CloudServiceAccountRequest> context) {

    return DeleteControl.DEFAULT_DELETE;
  }

  @Override
  public UpdateControl<CloudServiceAccountRequest> createOrUpdateResource(
      CloudServiceAccountRequest resource, Context<CloudServiceAccountRequest> context) {
    ConditionUtil.initializeConditions(resource);
    try {

      var accessTokenSecretName = resource.getSpec().getAccessTokenSecretName();
      var namespace = resource.getMetadata().getNamespace();

      String accessToken = null;

      accessToken = accessTokenSecretTool.getAccessToken(accessTokenSecretName, namespace);

      var serviceAccount = apiClient.createServiceAccount(resource.getSpec(), accessToken);

      apiClient.createSecretForServiceAccount(resource, serviceAccount);

      var status = resource.getStatus();
      status.setMessage("Created");
      status.setUpdated(Instant.now().toString());
      status.setServiceAccountSecretName(resource.getSpec().getServiceAccountSecretName());

      resource.setStatus(status);
      ConditionUtil.setAllConditionsTrue(resource.getStatus().getConditions());

      kafkaClientFactory
          .cloudServiceAccountRequest()
          .inNamespace(resource.getMetadata().getNamespace())
          .updateStatus(resource);
    } catch (ConditionAwareException e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
      ConditionUtil.setConditionFromException(resource.getStatus().getConditions(), e);

      kafkaClientFactory
          .cloudServiceAccountRequest()
          .inNamespace(resource.getMetadata().getNamespace())
          .updateStatus(resource);
    }

    return UpdateControl.noUpdate();
  }
}
