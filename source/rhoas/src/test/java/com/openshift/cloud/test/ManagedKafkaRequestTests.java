package com.openshift.cloud.test;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/** This tests that we have all the features for managed kafka request objects */
public class ManagedKafkaRequestTests {

  /**
   * The operator should create a managed kfaka request cr that contains all kafkas the service
   * account has access to.
   */
  @Test
  public void operatorCreatesMKRStatus() {
    fail();
  }

  @Test
  public void performTokenUpdate() {
    fail();
  }

  @Test
  public void watchServiceAccountSecret() {
    fail();
  }
}
