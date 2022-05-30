package com.openshift.cloud.test.util;

import io.fabric8.kubernetes.client.CustomResource;
import io.javaoperatorsdk.operator.api.config.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.RetryInfo;
import io.javaoperatorsdk.operator.api.reconciler.dependent.managed.ManagedDependentResourceContext;

import java.util.Optional;
import java.util.Set;

public class EmptyContext {
  public static <T extends CustomResource> Context<T> emptyContext(Class<T> clazz) {
    return new Context<T>() {
      @Override
      public Optional<RetryInfo> getRetryInfo() {
        return Optional.empty();
      }

      @Override
      public <T1> Optional<T1> getSecondaryResource(Class<T1> expectedType) {
        return Context.super.getSecondaryResource(expectedType);
      }

      @Override
      public <T1> Set<T1> getSecondaryResources(Class<T1> aClass) {
        return null;
      }

      @Override
      public <T1> Optional<T1> getSecondaryResource(Class<T1> aClass, String s) {
        return Optional.empty();
      }

      @Override
      public ControllerConfiguration<T> getControllerConfiguration() {
        return null;
      }

      @Override
      public ManagedDependentResourceContext managedDependentResourceContext() {
        return null;
      }
    };
  }
}
