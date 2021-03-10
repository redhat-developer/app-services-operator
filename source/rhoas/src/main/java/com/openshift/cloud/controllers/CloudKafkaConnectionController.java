package com.openshift.cloud.controllers;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.KafkaApiClient;
import com.openshift.cloud.v1alpha.models.KafkaConnection;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

@Controller
public class KafkaConnectionController
    implements ResourceController<KafkaConnection> {

  private static final Logger LOG =
      Logger.getLogger(KafkaConnectionController.class.getName());

  @Inject KafkaApiClient apiClient;

  @Inject AccessTokenSecretTool accessTokenSecretTool;

  @Override
  public DeleteControl deleteResource(
      KafkaConnection resource, Context<KafkaConnection> context) {
    LOG.info(String.format("Deleting resource %s", resource.getMetadata().getName()));

    return DeleteControl.DEFAULT_DELETE;
  }

  @Override
  public UpdateControl<KafkaConnection> createOrUpdateResource(
      KafkaConnection resource, Context<KafkaConnection> context) {
    ConditionUtil.initializeConditions(resource);
    try {
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
      status.setUiRefForKafkaId(kafkaId);
      status.setSaslMechanism("PLAIN");
      status.setSecurityProtocol("SASL_SSL");

      ConditionUtil.setAllConditionsTrue(resource.getStatus().getConditions());
    } catch (ConditionAwareException e) {
      LOG.log(Level.SEVERE, "Setting condition for exception " + e.getReason(), e);
      ConditionUtil.setConditionFromException(resource.getStatus().getConditions(), e);
    }
    return UpdateControl.updateStatusSubResource(resource);
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
}
