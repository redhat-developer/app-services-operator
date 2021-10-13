package com.openshift.cloud.test.util;

import io.fabric8.kubernetes.client.CustomResource;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.RetryInfo;
import io.javaoperatorsdk.operator.processing.event.EventList;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import java.util.Optional;

public class EmptyContext {
  public static <T extends CustomResource> Context<T> emptyContext(Class<T> clazz) {
    return new Context<T>() {

      @Override
      public EventList getEvents() {
        return null;
      }

      @Override
      public Optional<RetryInfo> getRetryInfo() {
        return Optional.empty();
      }
    };
  }
}
