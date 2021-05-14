package com.openshift.cloud.beans;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import com.openshift.cloud.v1alpha.models.KafkaCondition;
import com.openshift.cloud.api.ApiClient;
import com.openshift.cloud.api.ApiException;
import com.openshift.cloud.api.Configuration;
import com.openshift.cloud.api.srs.RegistriesApi;
import com.openshift.cloud.api.srs.models.Registry;
import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.api.auth.HttpBearerAuth;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class SchemaRegistryApiClient {

  @ConfigProperty(name = "rhoas.client.apiBasePath")
  String clientBasePath;

  private RegistriesApi createRegistriesClient(String bearerToken) {

    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath(clientBasePath);

    // Configure HTTP bearer authorization: Bearer
    HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
    Bearer.setBearerToken(bearerToken);

    return new RegistriesApi(defaultClient);
  }

  public List<Registry> listRegistries(String accessToken) throws ConditionAwareException {
    try {
      return createRegistriesClient(accessToken).getRegistries();
    } catch (ApiException e) {
      String message = ConditionUtil.getStandarizedErrorMessage(e);
      throw new ConditionAwareException(message, e, KafkaCondition.Type.ServiceRegistriesUpToDate,
          KafkaCondition.Status.False, e.getClass().getName(), message);
    }

  }

}
