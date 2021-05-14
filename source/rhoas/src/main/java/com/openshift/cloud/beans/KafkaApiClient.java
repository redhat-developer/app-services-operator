package com.openshift.cloud.beans;

import com.openshift.cloud.api.ApiClient;
import com.openshift.cloud.api.ApiException;
import com.openshift.cloud.api.Configuration;
import com.openshift.cloud.api.auth.HttpBearerAuth;
import com.openshift.cloud.api.kas.DefaultApi;
import com.openshift.cloud.api.kas.models.KafkaRequest;
import com.openshift.cloud.api.kas.models.KafkaRequestList;
import com.openshift.cloud.api.kas.models.ServiceAccount;
import com.openshift.cloud.api.kas.models.ServiceAccountRequest;
import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequest;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequestSpec;
import com.openshift.cloud.v1alpha.models.KafkaCondition;
import io.fabric8.kubernetes.api.model.OwnerReferenceBuilder;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class KafkaApiClient {

  @Inject KubernetesClient k8sClient;

  @ConfigProperty(name = "rhoas.client.apiBasePath")
  String clientBasePath;

  private DefaultApi createClient(String bearerToken) {

    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath(clientBasePath);

    // Configure HTTP bearer authorization: Bearer
    HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
    Bearer.setBearerToken(bearerToken);

    return new DefaultApi(defaultClient);
  }

  public KafkaRequest getKafkaById(String kafkaId, String accessToken)
      throws ConditionAwareException {
    try {
      return createClient(accessToken).getKafkaById(kafkaId);
    } catch (ApiException e) {
      String message = ConditionUtil.getStandarizedErrorMessage(e);
      throw new ConditionAwareException(
          message,
          e,
          KafkaCondition.Type.FoundKafkaById,
          KafkaCondition.Status.False,
          e.getClass().getName(),
          message);
    }
  }

  public KafkaRequestList listKafkas(String accessToken) throws ConditionAwareException {
    try {
      return createClient(accessToken).listKafkas(null, null, null, null);
    } catch (ApiException e) {
      String message = ConditionUtil.getStandarizedErrorMessage(e);
      throw new ConditionAwareException(
          message,
          e,
          KafkaCondition.Type.UserKafkasUpToDate,
          KafkaCondition.Status.False,
          e.getClass().getName(),
          message);
    }
  }

  public ServiceAccount createServiceAccount(
      CloudServiceAccountRequestSpec spec, String accessToken) throws ConditionAwareException {
    try {
      var serviceAccountRequest = new ServiceAccountRequest();
      serviceAccountRequest.setDescription(spec.getServiceAccountDescription());
      serviceAccountRequest.setName(spec.getServiceAccountName());
      return createClient(accessToken).createServiceAccount(serviceAccountRequest);
    } catch (ApiException e) {
      String message = ConditionUtil.getStandarizedErrorMessage(e);
      throw new ConditionAwareException(
          message,
          e,
          KafkaCondition.Type.ServiceAccountCreated,
          KafkaCondition.Status.False,
          e.getClass().getName(),
          message);
    }
  }

  public void createSecretForServiceAccount(
      CloudServiceAccountRequest resource, ServiceAccount serviceAccount)
      throws ConditionAwareException {
    try {
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
    } catch (Exception e) {
      throw new ConditionAwareException(
          e.getMessage(),
          e,
          KafkaCondition.Type.ServiceAccountSecretCreated,
          KafkaCondition.Status.False,
          e.getClass().getName(),
          e.getMessage());
    }
  }
}
