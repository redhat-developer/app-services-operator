package com.openshift.cloud.controllers;

import com.openshift.cloud.ApiClient;
import com.openshift.cloud.ApiException;
import com.openshift.cloud.Configuration;
import com.openshift.cloud.api.DefaultApi;
import com.openshift.cloud.auth.HttpBearerAuth;
import com.openshift.cloud.beans.ManagedKafkaK8sClients;
import com.openshift.cloud.beans.TokenExchanger;
import com.openshift.cloud.v1alpha.models.ManagedKafkaRequest;
import com.openshift.cloud.v1alpha.models.ManagedKafkaRequestStatus;
import com.openshift.cloud.v1alpha.models.UserKafka;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import io.quarkus.scheduler.Scheduled;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Controller
public class ManagedKafkaRequestController implements ResourceController<ManagedKafkaRequest> {

  private static final Logger LOG = Logger.getLogger(ManagedKafkaRequestController.class.getName());

  @Inject KubernetesClient k8sClient;

  @Inject TokenExchanger tokenExchanger;

  @Inject ManagedKafkaK8sClients managedKafkaClientFactory;

  @ConfigProperty(name = "client.basePath", defaultValue = "https://api.stage.openshift.com")
  String clientBasePath;

  public ManagedKafkaRequestController() {}

  @Override
  public DeleteControl deleteResource(
      ManagedKafkaRequest managedKafkaRequest, Context<ManagedKafkaRequest> context) {
    LOG.info(String.format("Deleting resource %s", managedKafkaRequest.getMetadata().getName()));

    return DeleteControl.DEFAULT_DELETE;
  }

  @Override
  public UpdateControl<ManagedKafkaRequest> createOrUpdateResource(
      ManagedKafkaRequest resource, Context<ManagedKafkaRequest> context) {
    LOG.info(String.format("Update or create resource %s", resource.getMetadata().getName()));

    try {
      updateManagedKafkaRequest(resource);
      var mkClient = managedKafkaClientFactory.managedKafkaRequest();
      mkClient.inNamespace(resource.getMetadata().getNamespace()).createOrReplace(resource);

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
  private boolean updateManagedKafkaRequest(ManagedKafkaRequest resource) throws ApiException {
    var saSecretName = resource.getSpec().getAccessTokenSecretName();
    var namespace = resource.getMetadata().getNamespace();
    var saSecret =
        k8sClient
            .secrets()
            .inNamespace(namespace)
            .withName(saSecretName)
            .get()
            .getData()
            .get("value"); // TODO: what is the secret format?
    saSecret = new String(Base64.getDecoder().decode(saSecret));
    saSecret = tokenExchanger.getToken(saSecret);

    var kafkaList = createClient(saSecret).listKafkas(null, null, null, null);

    var userKafkas = new HashMap<String, UserKafka>();

    kafkaList.getItems().stream()
        .forEach(
            listItem -> {
              var userKafka = new UserKafka();
              userKafka.setId(listItem.getId());
              userKafka.setCreated(listItem.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME));
              userKafka.setOwner(listItem.getOwner());
              userKafka.setProvider(listItem.getCloudProvider());
              userKafka.setRegion(listItem.getRegion());

              userKafkas.put(listItem.getName(), userKafka);
            });

    if (resource.getStatus() != null && userKafkas.equals(resource.getStatus().getUserKafkas())) {
      return false;
    }

    var status =
        new ManagedKafkaRequestStatus(
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), userKafkas);
    resource.setStatus(status);
    return true;
  }

  @Scheduled(every = "60s")
  void reloadUserKafkas() {
    LOG.info("Refreshing user kafkas");

    var mkClient = managedKafkaClientFactory.managedKafkaRequest();

    mkClient
        .list()
        .getItems()
        .forEach(
            resource -> {
              try {
                updateManagedKafkaRequest(resource);
                mkClient.createOrReplace(resource);
              } catch (ApiException e) {
                e.printStackTrace();
              }
            });
  }

  private DefaultApi createClient(String bearerToken) {

    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath(clientBasePath);

    // Configure HTTP bearer authorization: Bearer
    HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
    Bearer.setBearerToken(bearerToken);

    return new DefaultApi(defaultClient);
  }

  @Override
  public void init(EventSourceManager eventSourceManager) {
    LOG.info("Init! This is where we would add watches for child resources");
  }
}
