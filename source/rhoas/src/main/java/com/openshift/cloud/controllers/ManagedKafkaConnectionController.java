package com.openshift.cloud.controllers;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.ManagedKafkaApiClient;
import com.openshift.cloud.v1alpha.models.BoostrapServer;
import com.openshift.cloud.v1alpha.models.ManagedKafkaConnection;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.api.config.ControllerConfiguration;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

@Controller(namespaces = ControllerConfiguration.WATCH_ALL_NAMESPACES_MARKER)
public class ManagedKafkaConnectionController
    implements ResourceController<ManagedKafkaConnection> {

  private static final Logger LOG =
      Logger.getLogger(ManagedKafkaConnectionController.class.getName());

  @Inject ManagedKafkaApiClient apiClient;

  @Inject AccessTokenSecretTool accessTokenSecretTool;

  @Override
  public DeleteControl deleteResource(
      ManagedKafkaConnection resource, Context<ManagedKafkaConnection> context) {
    LOG.info(String.format("Deleting resource %s", resource.getMetadata().getName()));

    return DeleteControl.DEFAULT_DELETE;
  }

  @Override
  public UpdateControl<ManagedKafkaConnection> createOrUpdateResource(
      ManagedKafkaConnection resource, Context<ManagedKafkaConnection> context) {
    ConditionUtil.initializeConditions(resource);
    try {
      LOG.info(String.format("Creating or Updating resource %s", resource.getMetadata().getName()));

      var kafkaId = resource.getSpec().getKafkaId();
      var accessTokenSecretName = resource.getSpec().getAccessTokenSecretName();
      var serviceAccountSecretName =
          resource.getSpec().getCredentials().getServiceAccountSecretName();
      var namespace = resource.getMetadata().getNamespace();

      String accessToken =
          accessToken = accessTokenSecretTool.getAccessToken(accessTokenSecretName, namespace);

      var kafkaServiceInfo = apiClient.getKafkaById(kafkaId, accessToken);

      var bootStrapHost = kafkaServiceInfo.getBootstrapServerHost();
      var bootStrapServer = new BoostrapServer(bootStrapHost);

      var status = resource.getStatus();
      status.setMessage("Created");
      status.setUpdated(Instant.now().toString());
      status.setBoostrapServer(bootStrapServer);
      status.setServiceAccountSecretName(serviceAccountSecretName);
      status.setSaslMechanism("SASL_SSL");
      status.setSecurityProtocol("PLAINTEXT");

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
