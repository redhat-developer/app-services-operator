package com.openshift.cloud.test.util;

import com.openshift.cloud.beans.AccessTokenSecretTool;
import com.openshift.cloud.controllers.ConditionAwareException;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;

/** Utility bean to exchange offline tokens for access tokens */
@Alternative
@Singleton
public class MockAccessTokenSecretTool extends AccessTokenSecretTool {

  @Override
  public String getAccessToken(String accessTokenSecretName, String namespace)
      throws ConditionAwareException {
    return "12345";
  }
}
