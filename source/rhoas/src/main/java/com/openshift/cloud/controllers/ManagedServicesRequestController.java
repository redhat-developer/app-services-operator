package com.openshift.cloud.controllers;

import com.openshift.cloud.ApiException;
import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.ManagedKafkaApiClient;
import com.openshift.cloud.beans.ManagedKafkaK8sClients;
import com.openshift.cloud.v1alpha.models.CloudServicesRequest;
import com.openshift.cloud.v1alpha.models.UserKafka;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

@Controller
public class CloudServicesRequestController
    implements ResourceController<CloudServicesRequest> {

  private static final Logger LOG =
      Logger.getLogger(CloudServicesRequestController.class.getName());

  @Inject AccessTokenSecretTool accessTokenSecretTool;

  @Inject ManagedKafkaK8sClients managedKafkaClientFactory;

  @Inject ManagedKafkaApiClient apiClient;

  public CloudServicesRequestController() {}

  @Override
  public DeleteControl deleteResource(
      CloudServicesRequest cloudServicesRequest, Context<CloudServicesRequest> context) {
    LOG.info(String.format("Deleting resource %s", cloudServicesRequest.getMetadata().getName()));

    return DeleteControl.DEFAULT_DELETE;
  }

  @Override
  public UpdateControl<CloudServicesRequest> createOrUpdateResource(
      CloudServicesRequest resource, Context<CloudServicesRequest> context) {

    LOG.info(String.format("Update or create resource %s", resource.getMetadata().getName()));

    try {
      updateCloudServicesRequest(resource);
      var mkClient = managedKafkaClientFactory.cloudServicesRequest();
      mkClient.inNamespace(resource.getMetadata().getNamespace()).updateStatus(resource);

      return UpdateControl.noUpdate();
    } catch (ApiException e) {
      e.printStackTrace();
    }
    return UpdateControl.noUpdate();
  }

  /**
   * @param resource the resource to check for new kafkas
   * @return true if there were changes, false otherwise
   * @throws ApiException if something goes wrong connecting to services
   */
  private boolean updateCloudServicesRequest(CloudServicesRequest resource)
      throws ApiException {

    ConditionUtil.initializeConditions(resource);
    try {
      var accessTokenSecretName = resource.getSpec().getAccessTokenSecretName();
      var namespace = resource.getMetadata().getNamespace();
      String accessToken = null;
      accessToken = accessTokenSecretTool.getAccessToken(accessTokenSecretName, namespace);

      var kafkaList = apiClient.listKafkas(accessToken);

      var userKafkas = new ArrayList<UserKafka>();

      kafkaList.getItems().stream()
          .forEach(
              listItem -> {
                var userKafka =
                    new UserKafka()
                        .setId(listItem.getId())
                        .setName(listItem.getName())
                        .setOwner(listItem.getOwner())
                        .setBootstrapServerHost(listItem.getBootstrapServerHost())
                        .setStatus(listItem.getStatus())
                        .setCreatedAt(listItem.getCreatedAt().toInstant().toString())
                        .setUpdatedAt(listItem.getUpdatedAt().toInstant().toString())
                        .setProvider(listItem.getCloudProvider())
                        .setRegion(listItem.getRegion());

                userKafkas.add(userKafka);
              });

      ConditionUtil.setAllConditionsTrue(resource.getStatus().getConditions());
      if (userKafkas.equals(resource.getStatus().getUserKafkas())) {
        return false;
      } else {
        resource.getStatus().setUserKafkas(userKafkas);
      }

      return true;
    } catch (ConditionAwareException e) {
      LOG.log(Level.SEVERE, "Setting condition for exception " + e.getReason(), e);
      ConditionUtil.setConditionFromException(resource.getStatus().getConditions(), e);
      return true;
    }
  }

  @Override
  public void init(EventSourceManager eventSourceManager) {
    LOG.info("Init! This is where we would add watches for child resources");
  }
}
