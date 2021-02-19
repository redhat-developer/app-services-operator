package com.openshift.cloud.test;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/** This tests that we have all the features for managed Services request objects */
public class ManagedServicesRequestTests {

  /**
   * The operator should create a managed kfaka request cr that contains all kafkas the service
   * account has access to.
   */
  @Test
  @Disabled
  public void operatorCreatesMSRStatus() {
    fail();
  }

  @Test
  @Disabled
  public void performTokenUpdate() {
    fail();
  }

  @Test
  @Disabled
  public void watchServiceAccountSecret() {
    fail();
  }
}
