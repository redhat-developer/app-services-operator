package com.openshift.cloud.controllers;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.beans.KafkaApiClient;
import com.openshift.cloud.beans.ServiceRegistryApiClient;
import com.openshift.cloud.utils.InvalidUserInputException;
import com.openshift.cloud.v1alpha.models.CloudServicesRequest;
import com.openshift.cloud.v1alpha.models.ServiceRegistry;
import com.openshift.cloud.v1alpha.models.UserKafka;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.logging.Logger;

@Controller
public class CloudServicesRequestController
    extends AbstractCloudServicesController<CloudServicesRequest> {

  private static final Logger LOG =
      Logger.getLogger(CloudServicesRequestController.class.getName());

  @Inject
  AccessTokenSecretTool accessTokenSecretTool;

  @Inject
  KafkaApiClient kasApiClient;

  @Inject
  ServiceRegistryApiClient srsApiClient;

  public CloudServicesRequestController() {}

  /**
   * @return true if there were changes, false otherwise
   **/
  @Override
  public void init(EventSourceManager eventSourceManager) {
    LOG.info("Init! This is where we would add watches for child resources");
  }

  @Override
  void doCreateOrUpdateResource(CloudServicesRequest resource,
      Context<CloudServicesRequest> context)
      throws ConditionAwareException, InvalidUserInputException {
    var accessTokenSecretName = resource.getSpec().getAccessTokenSecretName();
    var namespace = resource.getMetadata().getNamespace();
    validateResource(resource);
    String accessToken;
    accessToken = accessTokenSecretTool.getAccessToken(accessTokenSecretName, namespace);
    var kafkaList = kasApiClient.listKafkas(accessToken);
    var registryList = srsApiClient.listRegistries(accessToken);

    var userKafkas = new ArrayList<UserKafka>();

    kafkaList.getItems().stream().forEach(listItem -> {
      var userKafka = new UserKafka().setId(listItem.getId()).setName(listItem.getName())
          .setOwner(listItem.getOwner()).setBootstrapServerHost(listItem.getBootstrapServerHost())
          .setStatus(listItem.getStatus())
          .setCreatedAt(listItem.getCreatedAt().toInstant().toString())
          .setUpdatedAt(listItem.getUpdatedAt().toInstant().toString())
          .setProvider(listItem.getCloudProvider()).setRegion(listItem.getRegion());

      userKafkas.add(userKafka);
    });

    resource.getStatus().setUserKafkas(userKafkas);

    var registries = new ArrayList<ServiceRegistry>();

    registryList.forEach(listItem -> {
      var serviceRegistry = new ServiceRegistry().setId(listItem.getId())
          .setName(listItem.getName()).setRegistryUrl(listItem.getRegistryUrl())
          .setRegistryStatus(listItem.getStatus().getValue())
          .setLastUpdated(listItem.getUpdatedAt().toInstant().toString());

      registries.add(serviceRegistry);
    });

    resource.getStatus().setServiceRegistries(registries);
  }

  void validateResource(CloudServicesRequest resource) throws InvalidUserInputException {
    ConditionUtil.assertNotNull(resource.getSpec(), "spec");
    ConditionUtil.assertNotNull(resource.getSpec().getAccessTokenSecretName(),
        "spec.accessTokenSecretName");
    ConditionUtil.assertNotNull(resource.getMetadata().getNamespace(), "metadata.namespace");
  }
}
