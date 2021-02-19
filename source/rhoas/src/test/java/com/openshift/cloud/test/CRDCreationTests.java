package com.openshift.cloud.test;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/** Per ADR-00022 the operator should create CRDs when it runs if they do not exist. */
public class CRDCreationTests {
  @Test
  @Disabled
  public void createManagedKafkaConnectionCRDOnInit() {
    fail();
  }

  @Test
  @Disabled
  public void createManagedServicesRequestCRDOnInit() {
    fail();
  }

  @Test
  @Disabled
  public void createManagedKafkaServiceAccountCRDOnInit() {
    fail();
  }
}
