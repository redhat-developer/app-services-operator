package com.openshift.cloud.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Per ADR-00022 the operator should create CRDs when it runs if they do not exist.
 */
public class CRDCreationTests {
    @Test
    public void createManagedKafkaConnectionCRDOnInit() {
        fail();
    }

    @Test
    public void createManagedKafkaRequestCRDOnInit() {
        fail();
    }

    @Test
    public void createManagedKafkaServiceAccountCRDOnInit() {
        fail();
    }

}
