package com.openshift.cloud.controllers;

import com.openshift.cloud.ApiException;
import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.ManagedKafkaApiClient;
import com.openshift.cloud.beans.ManagedKafkaK8sClients;
import com.openshift.cloud.v1alpha.models.ManagedServicesRequest;
import com.openshift.cloud.v1alpha.models.UserKafka;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.api.config.ControllerConfiguration;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import io.quarkus.scheduler.Scheduled;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

@Controller(namespaces = ControllerConfiguration.WATCH_ALL_NAMESPACES_MARKER)
public class ManagedServicesRequestController
    implements ResourceController<ManagedServicesRequest> {

  private static final Logger LOG =
      Logger.getLogger(ManagedServicesRequestController.class.getName());

  @Inject AccessTokenSecretTool accessTokenSecretTool;

  @Inject ManagedKafkaK8sClients managedKafkaClientFactory;

  @Inject ManagedKafkaApiClient apiClient;

  public ManagedServicesRequestController() {}

  @Override
  public DeleteControl deleteResource(
      ManagedServicesRequest managedServicesRequest, Context<ManagedServicesRequest> context) {
    LOG.info(String.format("Deleting resource %s", managedServicesRequest.getMetadata().getName()));

    return DeleteControl.DEFAULT_DELETE;
  }

  @Override
  public UpdateControl<ManagedServicesRequest> createOrUpdateResource(
      ManagedServicesRequest resource, Context<ManagedServicesRequest> context) {

    LOG.info(String.format("Update or create resource %s", resource.getMetadata().getName()));

    try {
      updateManagedServicesRequest(resource);
      var mkClient = managedKafkaClientFactory.managedServicesRequest();
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
  private boolean updateManagedServicesRequest(ManagedServicesRequest resource)
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

  @Scheduled(every = "150s")
  void reloadUserKafkas() {
    LOG.info("Refreshing user kafkas");

    var mkClient = managedKafkaClientFactory.managedServicesRequest();
    var items = mkClient.inAnyNamespace().list().getItems();
    LOG.info("Items to refresh" + items.size());
    items.forEach(
        resource -> {
          try {

            // If there are untrue conditions, always update the status
            if (!ConditionUtil.allTrue(resource.getStatus().getConditions())
                || updateManagedServicesRequest(resource)) {
              mkClient.inNamespace(resource.getMetadata().getNamespace()).updateStatus(resource);
              LOG.info("refreshed kafka" + resource.getMetadata().getName());
            }

          } catch (ApiException e) {
            e.printStackTrace();
          }
        });
  }

  @Override
  public void init(EventSourceManager eventSourceManager) {
    LOG.info("Init! This is where we would add watches for child resources");
  }
}
