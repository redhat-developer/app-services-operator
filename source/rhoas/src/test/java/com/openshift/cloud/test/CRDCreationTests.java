package com.openshift.cloud.test;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/** Per ADR-00022 the operator should create CRDs when it runs if they do not exist. */
public class CRDCreationTests {
  @Test
  @Disabled
  public void createKafkaConnectionCRDOnInit() {
    fail();
  }

  @Test
  @Disabled
  public void createCloudServicesRequestCRDOnInit() {
    fail();
  }

  @Test
  @Disabled
  public void createKafkaServiceAccountCRDOnInit() {
    fail();
  }
}
