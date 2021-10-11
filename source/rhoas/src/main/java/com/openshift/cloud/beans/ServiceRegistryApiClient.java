package com.openshift.cloud.beans;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition;
import com.openshift.cloud.api.srs.invoker.ApiClient;
import com.openshift.cloud.api.srs.invoker.ApiException;
import com.openshift.cloud.api.srs.invoker.Configuration;
import com.openshift.cloud.api.srs.RegistriesApi;
import com.openshift.cloud.api.srs.models.*;
import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.api.srs.invoker.auth.HttpBearerAuth;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ServiceRegistryApiClient {

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

  public List<RegistryRest> listRegistries(String accessToken) throws ConditionAwareException {
    try {
      return createRegistriesClient(accessToken).getRegistries(null, null, null, null).getItems();
    } catch (ApiException e) {
      String message = ConditionUtil.getStandarizedErrorMessage(e.getCode(), e);
      throw new ConditionAwareException(message, e,
          CloudServiceCondition.Type.ServiceRegistriesUpToDate, CloudServiceCondition.Status.False,
          e.getClass().getName(), message);
    }

  }

  public RegistryRest getServiceRegistryById(String registryId, String accessToken)
      throws ConditionAwareException {
    try {
      return createRegistriesClient(accessToken).getRegistry(registryId);
    } catch (ApiException e) {
      String message = ConditionUtil.getStandarizedErrorMessage(e.getCode(), e);
      throw new ConditionAwareException(message, e,
          CloudServiceCondition.Type.FoundServiceRegistryById, CloudServiceCondition.Status.False,
          e.getClass().getName(), message);
    }
  }

}
