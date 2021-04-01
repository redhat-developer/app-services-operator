package com.openshift.cloud.controllers;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.KafkaApiClient;
import com.openshift.cloud.beans.KafkaK8sClients;
import com.openshift.cloud.utils.InvalidUserInputException;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequest;
import io.javaoperatorsdk.operator.api.*;
import java.time.Instant;
import javax.inject.Inject;

/** Controller for CloudServiceAccountRequest CRs */
@Controller
public class CloudServiceAccountRequestController
    extends AbstractCloudServicesController<CloudServiceAccountRequest> {

  @Inject AccessTokenSecretTool accessTokenSecretTool;

  @Inject KafkaK8sClients kafkaClientFactory;

  @Inject KafkaApiClient apiClient;

  @Override
  void doCreateOrUpdateResource(
      CloudServiceAccountRequest resource, Context<CloudServiceAccountRequest> context)
      throws ConditionAwareException, InvalidUserInputException {

    validateResource(resource);
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
  }

  void validateResource(CloudServiceAccountRequest resource) throws InvalidUserInputException {
    ConditionUtil.assertNotNull(resource.getSpec(), "spec");
    ConditionUtil.assertNotNull(
        resource.getSpec().getAccessTokenSecretName(), "spec.accessTokenSecretName");
    ConditionUtil.assertNotNull(
        resource.getSpec().getServiceAccountName(), "spec.serviceAccountName");
    ConditionUtil.assertNotNull(
        resource.getSpec().getServiceAccountSecretName(), "spec.serviceAccountSecretName");
    ConditionUtil.assertNotNull(resource.getMetadata().getNamespace(), "metadata.namespace");
  }
}
