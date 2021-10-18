package com.openshift.cloud.beans;

import com.openshift.cloud.api.kas.DefaultApi;
import com.openshift.cloud.api.kas.SecurityApi;
import com.openshift.cloud.api.kas.invoker.ApiClient;
import com.openshift.cloud.api.kas.invoker.ApiException;
import com.openshift.cloud.api.kas.invoker.Configuration;
import com.openshift.cloud.api.kas.invoker.auth.HttpBearerAuth;
import com.openshift.cloud.api.kas.models.KafkaRequest;
import com.openshift.cloud.api.kas.models.KafkaRequestList;
import com.openshift.cloud.api.kas.models.ServiceAccount;
import com.openshift.cloud.api.kas.models.ServiceAccountRequest;
import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequestSpec;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class KafkaApiClient {

  @Inject
  KubernetesClient k8sClient;

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
      String message = ConditionUtil.getStandarizedErrorMessage(e.getCode(), e);
      throw new ConditionAwareException(message, e, CloudServiceCondition.Type.FoundKafkaById,
          CloudServiceCondition.Status.False, e.getClass().getName(), message);
    }
  }

  public KafkaRequestList listKafkas(String accessToken) throws ConditionAwareException {
    try {
      return createClient(accessToken).getKafkas(null, null, null, null);
    } catch (ApiException e) {
      String message = ConditionUtil.getStandarizedErrorMessage(e.getCode(), e);
      throw new ConditionAwareException(message, e, CloudServiceCondition.Type.UserKafkasUpToDate,
          CloudServiceCondition.Status.False, e.getClass().getName(), message);
    }
  }

  public ServiceAccount createServiceAccount(CloudServiceAccountRequestSpec spec,
      String accessToken) throws ConditionAwareException {
    try {
      var serviceAccountRequest = new ServiceAccountRequest();
      serviceAccountRequest.setDescription(spec.getServiceAccountDescription());
      serviceAccountRequest.setName(spec.getServiceAccountName());
      return createSecurityClient(accessToken).createServiceAccount(serviceAccountRequest);
    } catch (ApiException e) {
      String message = ConditionUtil.getStandarizedErrorMessage(e.getCode(), e);
      throw new ConditionAwareException(message, e,
          CloudServiceCondition.Type.ServiceAccountCreated, CloudServiceCondition.Status.False,
          e.getClass().getName(), message);
    }
  }

  // TODO create ACLs

  private SecurityApi createSecurityClient(String bearerToken) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath(clientBasePath);

    // Configure HTTP bearer authorization: Bearer
    HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
    Bearer.setBearerToken(bearerToken);

    return new SecurityApi(defaultClient);
  }
}


