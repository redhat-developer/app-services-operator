package com.openshift.cloud.controllers;

import com.openshift.cloud.ApiClient;
import com.openshift.cloud.ApiException;
import com.openshift.cloud.Configuration;
import com.openshift.cloud.api.DefaultApi;
import com.openshift.cloud.api.models.ServiceAccountRequest;
import com.openshift.cloud.auth.HttpBearerAuth;
import com.openshift.cloud.beans.ManagedKafkaK8sClients;
import com.openshift.cloud.beans.TokenExchanger;
import com.openshift.cloud.v1alpha.models.ManagedServiceAccountRequest;
import com.openshift.cloud.v1alpha.models.ManagedServiceAccountRequestStatus;
import io.fabric8.kubernetes.api.model.OwnerReferenceBuilder;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.api.config.ControllerConfiguration;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Controller for ManagedServiceAccountRequest CRs
 */
@Controller(namespaces = ControllerConfiguration.WATCH_ALL_NAMESPACES_MARKER)
public class ManagedServiceAccountRequestController
    implements ResourceController<ManagedServiceAccountRequest> {
  public final String ACCESS_TOKEN_SECRET_KEY = "value";

  private static final Logger LOG =
      Logger.getLogger(ManagedKafkaConnectionController.class.getName());

  @Inject KubernetesClient k8sClient;

  @Inject TokenExchanger tokenExchanger;

  @Inject ManagedKafkaK8sClients managedKafkaClientFactory;

  @ConfigProperty(name = "client.basePath", defaultValue = "https://api.stage.openshift.com")
  String clientBasePath;

  @Override
  public DeleteControl deleteResource(
      ManagedServiceAccountRequest resource, Context<ManagedServiceAccountRequest> context) {

    return DeleteControl.DEFAULT_DELETE;
  }

  @Override
  public UpdateControl<ManagedServiceAccountRequest> createOrUpdateResource(
      ManagedServiceAccountRequest resource, Context<ManagedServiceAccountRequest> context) {

    if (resource.getStatus() != null) {
      return UpdateControl.noUpdate();
    }

    var apiTokenSecretName = resource.getSpec().getAccessTokenSecretName();
    var namespace = resource.getMetadata().getNamespace();

    var accessToken =
        k8sClient
            .secrets()
            .inNamespace(namespace)
            .withName(apiTokenSecretName)
            .get()
            .getData()
            .get(ACCESS_TOKEN_SECRET_KEY);
    accessToken = new String(Base64.getDecoder().decode(accessToken));
    accessToken = tokenExchanger.getToken(accessToken);

    var managedServiceClient = createClient(accessToken);
    var serviceAccountRequest = new ServiceAccountRequest();
    serviceAccountRequest.setDescription(resource.getSpec().getServiceAccountDescription());
    serviceAccountRequest.setName(resource.getSpec().getServiceAccountName());

    try {
      var serviceAccount = managedServiceClient.createServiceAccount(serviceAccountRequest);
      serviceAccount.getClientSecret();
      var status =
          new ManagedServiceAccountRequestStatus(
              "Created",
              Instant.now().toString(),
              resource.getSpec().getServiceAccountSecretName());
      resource.setStatus(status);

      var secret =
          new SecretBuilder()
              .editOrNewMetadata()
              .withName(resource.getSpec().getServiceAccountSecretName())
              .withNamespace(resource.getMetadata().getNamespace())
              .withOwnerReferences(
                  List.of(
                      new OwnerReferenceBuilder()
                          .withApiVersion(resource.getApiVersion())
                          .withController(true)
                          .withKind(resource.getKind())
                          .withName(resource.getMetadata().getName())
                          .withUid(resource.getMetadata().getUid())
                          .build()))
              .endMetadata()
              .withData(
                  Map.of(
                      "client-secret",
                      Base64.getEncoder()
                          .encodeToString(
                              serviceAccount.getClientSecret().getBytes(Charset.defaultCharset())),
                      "client-id",
                      Base64.getEncoder()
                          .encodeToString(
                              serviceAccount.getClientID().getBytes(Charset.defaultCharset()))))
              .build();

      k8sClient.secrets().inNamespace(secret.getMetadata().getNamespace()).create(secret);

      managedKafkaClientFactory
          .managedServiceAccountRequest()
          .inNamespace(resource.getMetadata().getNamespace())
          .updateStatus(resource);

      return UpdateControl.noUpdate();

    } catch (ApiException e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private DefaultApi createClient(String clientToken) {

    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath(clientBasePath);

    // Configure HTTP bearer authorization: Bearer
    HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
    Bearer.setBearerToken(clientToken);

    return new DefaultApi(defaultClient);
  }
}
