package com.openshift.cloud.beans;

import com.openshift.cloud.api.srs.invoker.ApiException;
import com.openshift.cloud.api.srs.models.RegistryRest;
import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition;
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

  public List<RegistryRest> listRegistries(String accessToken) throws ConditionAwareException {
    return new ArrayList<>();
  }

  public RegistryRest getServiceRegistryById(String registryId, String accessToken)
      throws ConditionAwareException {
    return new RegistryRest();
  }

}
