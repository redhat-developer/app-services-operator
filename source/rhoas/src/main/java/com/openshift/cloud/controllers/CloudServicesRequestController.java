package com.openshift.cloud.controllers;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.KafkaApiClient;
import com.openshift.cloud.beans.KafkaK8sClients;
import com.openshift.cloud.v1alpha.models.CloudServicesRequest;
import com.openshift.cloud.v1alpha.models.UserKafka;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.inject.Inject;

@Controller
public class CloudServicesRequestController
    extends AbstractCloudServicesController<CloudServicesRequest> {

  private static final Logger LOG =
      Logger.getLogger(CloudServicesRequestController.class.getName());

  @Inject AccessTokenSecretTool accessTokenSecretTool;

  @Inject KafkaK8sClients kafkaClientFactory;

  @Inject KafkaApiClient apiClient;

  public CloudServicesRequestController() {}

  /**
   * @param resource the resource to check for new kafkas
   * @return true if there were changes, false otherwise
   * @throws ApiException if something goes wrong connecting to services
   */
  @Override
  public void init(EventSourceManager eventSourceManager) {
    LOG.info("Init! This is where we would add watches for child resources");
  }

  @Override
  void doCreateOrUpdateResource(
      CloudServicesRequest resource, Context<CloudServicesRequest> context)
      throws ConditionAwareException {
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

    if (userKafkas.equals(resource.getStatus().getUserKafkas())) {

    } else {
      resource.getStatus().setUserKafkas(userKafkas);
    }
  }
}
