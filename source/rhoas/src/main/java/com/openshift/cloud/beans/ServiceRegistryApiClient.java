package com.openshift.cloud.beans;

import com.openshift.cloud.api.srs.RegistriesApi;
import com.openshift.cloud.api.srs.invoker.ApiClient;
import com.openshift.cloud.api.srs.invoker.ApiException;
import com.openshift.cloud.api.srs.invoker.Configuration;
import com.openshift.cloud.api.srs.invoker.auth.HttpBearerAuth;
import com.openshift.cloud.api.srs.models.Registry;
import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

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

  public List<Registry> listRegistries(String accessToken) throws ConditionAwareException {
    try {
      return createRegistriesClient(accessToken).getRegistries(null, null, null, null).getItems();
    } catch (ApiException e) {
      String message = ConditionUtil.getStandarizedErrorMessage(e.getCode(), e);
      throw new ConditionAwareException(message, e,
          CloudServiceCondition.Type.ServiceRegistriesUpToDate, CloudServiceCondition.Status.False,
          e.getClass().getName(), message);
    }

  }

  public Registry getServiceRegistryById(String registryId, String accessToken)
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
