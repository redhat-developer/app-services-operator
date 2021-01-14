package com.openshift.cloud;

import com.openshift.cloud.controllers.ManagedKafkaConnectionController;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.api.config.ConfigurationService;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.logging.Logger;
import javax.inject.Inject;

@QuarkusMain
public class RHOASOperator implements QuarkusApplication {

    @Inject
    KubernetesClient client;

    @Inject
    Operator operator;

    @Inject
    ConfigurationService configuration;

    @Inject
    ManagedKafkaConnectionController controller;

    private static final Logger LOG = Logger.getLogger(RHOASOperator.class);

    public static void main(String... args) {
        LOG.info("RHOAS Operator starting");
        Quarkus.run(RHOASOperator.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        final var config = configuration.getConfigurationFor(controller);
        LOG.info("CR class: " + config.getCustomResourceClass());
        Quarkus.waitForExit();
        return 0;
    }

}
