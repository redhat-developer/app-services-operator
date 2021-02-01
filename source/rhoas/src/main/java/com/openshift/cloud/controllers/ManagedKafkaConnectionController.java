package com.openshift.cloud.controllers;

import com.openshift.cloud.ApiClient;
import com.openshift.cloud.ApiException;
import com.openshift.cloud.Configuration;
import com.openshift.cloud.api.DefaultApi;
import com.openshift.cloud.auth.HttpBearerAuth;
import com.openshift.cloud.beans.TokenExchanger;
import com.openshift.cloud.v1alpha.models.BoostrapServer;
import com.openshift.cloud.v1alpha.models.ManagedKafkaConnection;
import com.openshift.cloud.v1alpha.models.ManagedKafkaConnectionStatus;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.api.config.ControllerConfiguration;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import java.time.Instant;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Controller(namespaces = ControllerConfiguration.WATCH_ALL_NAMESPACES_MARKER)
public class ManagedKafkaConnectionController
    implements ResourceController<ManagedKafkaConnection> {

  private static final Logger LOG =
      Logger.getLogger(ManagedKafkaConnectionController.class.getName());
  private final KubernetesClient k8sClient;
  public final String ACCESS_TOKEN_SECRET_KEY = "value";

  @ConfigProperty(name = "client.basePath", defaultValue = "https://api.stage.openshift.com")
  String clientBasePath;

  @Inject TokenExchanger tokenExchanger;

  public ManagedKafkaConnectionController(KubernetesClient k8sClient) {
    this.k8sClient = k8sClient;
  }

  @Override
  public DeleteControl deleteResource(
      ManagedKafkaConnection resource, Context<ManagedKafkaConnection> context) {
    LOG.info(String.format("Deleting resource %s", resource.getMetadata().getName()));

    return DeleteControl.DEFAULT_DELETE;
  }

  @Override
  public UpdateControl<ManagedKafkaConnection> createOrUpdateResource(
      ManagedKafkaConnection resource, Context<ManagedKafkaConnection> context) {
    LOG.info(String.format("Creating or Updating resource %s", resource.getMetadata().getName()));

    try {
      var kafkaId = resource.getSpec().getKafkaId();
      var saSecretName = resource.getSpec().getAccessTokenSecretName();
      var namespace = resource.getMetadata().getNamespace();

      var saSecret =
          k8sClient
              .secrets()
              .inNamespace(namespace)
              .withName(saSecretName)
              .get()
              .getData()
              .get(ACCESS_TOKEN_SECRET_KEY);
      saSecret = new String(Base64.getDecoder().decode(saSecret));
      saSecret = tokenExchanger.getToken(saSecret);

      var kafkaServiceInfo = createClient(saSecret).getKafkaById(kafkaId);

      var bootStrapHost = kafkaServiceInfo.getBootstrapServerHost();
      var bootStrapServer = new BoostrapServer(bootStrapHost);

      var status =
          new ManagedKafkaConnectionStatus(
              "Created", Instant.now().toString(), bootStrapServer, saSecretName);
      resource.setStatus(status);

      return UpdateControl.updateCustomResourceAndStatus(resource);
    } catch (ApiException e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
      return UpdateControl.noUpdate();
    }
  }

  private DefaultApi createClient(String bearerToken) {

    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath(clientBasePath);

    // Configure HTTP bearer authorization: Bearer
    HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
    Bearer.setBearerToken(bearerToken);

    return new DefaultApi(defaultClient);
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
