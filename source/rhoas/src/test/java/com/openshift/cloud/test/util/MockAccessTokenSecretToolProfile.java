package com.openshift.cloud.test.util;

import com.openshift.cloud.beans.MockAccessTokenSecretTool;
import com.openshift.cloud.beans.MockKafkaApiClient;
import com.openshift.cloud.beans.MockServiceRegistryApiClient;
import io.quarkus.test.junit.QuarkusTestProfile;
import java.util.Set;

public class MockAccessTokenSecretToolProfile implements QuarkusTestProfile {
  @Override
  public Set<Class<?>> getEnabledAlternatives() {

    return Set.of(MockAccessTokenSecretTool.class, MockKafkaApiClient.class, MockServiceRegistryApiClient.class);
  }
}
