package org.bf2.test.mock;

import io.quarkus.test.junit.QuarkusTestProfile;
import java.util.Map;

public class MockSyncProfile implements QuarkusTestProfile {

  @Override
  public Map<String, String> getConfigOverrides() {
    return Map.of("quarkus.scheduler.enabled", "false", "rhoas.client.useMocks", "true");
  }
}
