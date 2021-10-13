package com.openshift.cloud.beans;

import com.openshift.cloud.api.srs.models.Registry;
import com.openshift.cloud.controllers.ConditionAwareException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Alternative
public class MockServiceRegistryApiClient extends ServiceRegistryApiClient {

  @ConfigProperty(name = "rhoas.client.apiBasePath")
  String clientBasePath;

  public List<Registry> listRegistries(String accessToken) throws ConditionAwareException {
    return new ArrayList<>();
  }

  public Registry getServiceRegistryById(String registryId, String accessToken)
      throws ConditionAwareException {
    return new Registry();
  }

}
