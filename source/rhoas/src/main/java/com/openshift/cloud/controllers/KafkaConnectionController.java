package com.openshift.cloud.controllers;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.KafkaApiClient;
import com.openshift.cloud.beans.ServiceAccountUtil;
import com.openshift.cloud.utils.ConnectionResourcesMetadata;
import com.openshift.cloud.utils.InvalidUserInputException;
import com.openshift.cloud.v1alpha.models.KafkaConnection;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;

import javax.inject.Inject;
import java.time.Instant;
import java.util.logging.Logger;

@Controller
public class KafkaConnectionController extends AbstractCloudServicesController<KafkaConnection> {

  private static final Logger LOG = Logger.getLogger(KafkaConnectionController.class.getName());

  @Inject
  KafkaApiClient apiClient;

  @Inject
  AccessTokenSecretTool accessTokenSecretTool;

  @Inject
  ServiceAccountUtil serviceAccountUtil;

  @Override
  void doCreateOrUpdateResource(KafkaConnection resource, Context<KafkaConnection> context)
      throws ConditionAwareException, InvalidUserInputException {
    LOG.info(String.format("Creating or Updating resource %s", resource.getMetadata().getName()));

    validateResource(resource);
    var kafkaId = resource.getSpec().getKafkaId();
    var accessTokenSecretName = resource.getSpec().getAccessTokenSecretName();
    var serviceAccountSecretName =
        resource.getSpec().getCredentials().getServiceAccountSecretName();
    var namespace = resource.getMetadata().getNamespace();

    String accessToken = accessTokenSecretTool.getAccessToken(accessTokenSecretName, namespace);

    var kafkaServiceInfo = apiClient.getKafkaById(kafkaId, accessToken);

    var bootStrapHost = kafkaServiceInfo.getBootstrapServerHost();

    // var principal = serviceAccountUtil.getServiceAccountSecret(serviceAccountSecretName,
    // namespace);
    // TODO add ACL to principal

    var status = resource.getStatus();
    status.setMessage("Created");
    status.setUpdated(Instant.now().toString());
    status.setBootstrapServerHost(bootStrapHost);
    status.setServiceAccountSecretName(serviceAccountSecretName);
    status.setMetadata(ConnectionResourcesMetadata.buildKafkaMetadata(kafkaId));
  }


  void validateResource(KafkaConnection resource) throws InvalidUserInputException {
    ConditionUtil.assertNotNull(resource.getSpec(), "spec");
    ConditionUtil.assertNotNull(resource.getSpec().getAccessTokenSecretName(),
        "spec.accessTokenSecretName");
    ConditionUtil.assertNotNull(resource.getSpec().getCredentials(), "spec.credentials");
    ConditionUtil.assertNotNull(resource.getSpec().getCredentials().getServiceAccountSecretName(),
        "spec.credentials.serviceAccountSecretName");
    ConditionUtil.assertNotNull(resource.getSpec().getKafkaId(), "spec.kafkaId");
    ConditionUtil.assertNotNull(resource.getMetadata().getNamespace(), "metadata.namespace");
  }
}
