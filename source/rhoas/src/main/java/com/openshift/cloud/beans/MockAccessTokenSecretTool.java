package com.openshift.cloud.beans;

import com.openshift.cloud.controllers.ConditionAwareException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

/** Utility bean to exchange offline tokens for access tokens */
@Alternative
@ApplicationScoped
public class MockAccessTokenSecretTool extends AccessTokenSecretTool {

  @Override
  public String getAccessToken(String accessTokenSecretName, String namespace)
      throws ConditionAwareException {
    return "12345";
  }
}
