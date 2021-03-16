package com.openshift.cloud.controllers;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.KafkaApiClient;
import com.openshift.cloud.utils.ConnectionResourcesMetadata;
import com.openshift.cloud.v1alpha.models.KafkaConnection;
import io.javaoperatorsdk.operator.api.*;
import java.time.Instant;
import java.util.logging.Logger;
import javax.inject.Inject;

@Controller
public class KafkaConnectionController extends AbstractCloudServicesController<KafkaConnection> {

  private static final Logger LOG = Logger.getLogger(KafkaConnectionController.class.getName());

  @Inject KafkaApiClient apiClient;

  @Inject AccessTokenSecretTool accessTokenSecretTool;

  @Override
  void doCreateOrUpdateResource(KafkaConnection resource, Context<KafkaConnection> context)
      throws ConditionAwareException {
    LOG.info(String.format("Creating or Updating resource %s", resource.getMetadata().getName()));

    var kafkaId = resource.getSpec().getKafkaId();
    var accessTokenSecretName = resource.getSpec().getAccessTokenSecretName();
    var serviceAccountSecretName =
        resource.getSpec().getCredentials().getServiceAccountSecretName();
    var namespace = resource.getMetadata().getNamespace();

    String accessToken = accessTokenSecretTool.getAccessToken(accessTokenSecretName, namespace);

    var kafkaServiceInfo = apiClient.getKafkaById(kafkaId, accessToken);

    var bootStrapHost = kafkaServiceInfo.getBootstrapServerHost();

    var status = resource.getStatus();
    status.setMessage("Created");
    status.setUpdated(Instant.now().toString());
    status.setBootstrapServerHost(bootStrapHost);
    status.setServiceAccountSecretName(serviceAccountSecretName);
    status.setMetadata(ConnectionResourcesMetadata.buildKafkaMetadata(kafkaId));
  }
}
