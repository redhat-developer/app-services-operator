package com.openshift.cloud.beans;

import com.openshift.cloud.api.kas.DefaultApi;
import com.openshift.cloud.api.kas.invoker.ApiClient;
import com.openshift.cloud.api.kas.invoker.ApiException;
import com.openshift.cloud.api.kas.invoker.Configuration;
import com.openshift.cloud.api.kas.invoker.auth.HttpBearerAuth;
import com.openshift.cloud.api.kas.models.KafkaRequest;
import com.openshift.cloud.api.kas.models.KafkaRequestList;
import com.openshift.cloud.api.serviceaccounts.ServiceAccountsApi;
import com.openshift.cloud.api.serviceaccounts.models.ServiceAccountCreateRequestData;
import com.openshift.cloud.api.serviceaccounts.models.ServiceAccountData;
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

  @ConfigProperty(name = "auth.serverUrl")
  String saAPIURL;


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

  public ServiceAccountData createServiceAccount(CloudServiceAccountRequestSpec spec,
      String accessToken) throws ConditionAwareException {
    try {
      var serviceAccountRequest = new ServiceAccountCreateRequestData();
      serviceAccountRequest.setDescription(spec.getServiceAccountDescription());
      serviceAccountRequest.setName(spec.getServiceAccountName());
      return createSecurityClient(accessToken).createServiceAccount(serviceAccountRequest);
    } catch (com.openshift.cloud.api.serviceaccounts.invoker.ApiException e) {
      String message = ConditionUtil.getStandarizedErrorMessage(e.getCode(), e);
      throw new ConditionAwareException(message, e,
          CloudServiceCondition.Type.ServiceAccountCreated, CloudServiceCondition.Status.False,
          e.getClass().getName(), message);
    }
  }

  private ServiceAccountsApi createSecurityClient(String bearerToken) {
    var defaultClient = new com.openshift.cloud.api.serviceaccounts.invoker.ApiClient();
    defaultClient.setBasePath(saAPIURL);

    // Configure HTTP bearer authorization: Bearer
    HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
    Bearer.setBearerToken(bearerToken);

    return new ServiceAccountsApi(defaultClient);
  }
}


