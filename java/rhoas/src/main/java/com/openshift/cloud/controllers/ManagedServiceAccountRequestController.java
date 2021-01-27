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
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.DeleteControl;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.commons.lang3.NotImplementedException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Controller
public class ManagedServiceAccountRequestController
    implements ResourceController<ManagedServiceAccountRequest> {

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

    if (resource.getSpec().getReset()) {
      throw new NotImplementedException("Reset is not implemented");
    } else {
      var managedServiceClient = createClient(resource.getSpec().getAccessTokenSecretName());
      var serviceAccountRequest = new ServiceAccountRequest();
      serviceAccountRequest.setDescription(resource.getSpec().getDescription());
      serviceAccountRequest.setName(resource.getSpec().getServiceAccountName());

      try {
        var serviceAccount = managedServiceClient.createServiceAccount(serviceAccountRequest);
        serviceAccount.getClientSecret();
        var status =
            new ManagedServiceAccountRequestStatus(
                "Created",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
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
                                serviceAccount
                                    .getClientSecret()
                                    .getBytes(Charset.defaultCharset())),
                        "client-id",
                        Base64.getEncoder()
                            .encodeToString(
                                serviceAccount.getClientID().getBytes(Charset.defaultCharset()))))
                .build();

        k8sClient.secrets().create(secret);
        managedKafkaClientFactory.managedServiceAccountRequest().createOrReplace(resource);
        return UpdateControl.noUpdate();

      } catch (ApiException e) {
        LOG.log(Level.SEVERE, e.getMessage(), e);
        throw new RuntimeException(e.getMessage(), e);
      }
    }
  }

  private DefaultApi createClient(String clientToken) {

    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath(clientBasePath);

    // Configure HTTP bearer authorization: Bearer
    HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");

    clientToken = tokenExchanger.getToken(clientToken);
    Bearer.setBearerToken(clientToken);

    return new DefaultApi(defaultClient);
  }
}
