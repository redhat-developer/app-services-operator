package com.openshift.cloud.test.util;

import com.openshift.cloud.v1alpha.models.KafkaConnection;
import io.fabric8.kubernetes.client.CustomResource;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.RetryInfo;
import io.javaoperatorsdk.operator.processing.event.EventList;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;

import java.util.Optional;

public class EmptyContext {
    public static final Context EMPTY_CONTEXT = new Context() {
        @Override
        public EventSourceManager getEventSourceManager() {
            return null;
        }

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
