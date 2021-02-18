package com.openshift.cloud;

import com.openshift.cloud.controllers.ManagedKafkaConnectionController;
import com.openshift.cloud.controllers.ManagedServiceAccountRequestController;
import com.openshift.cloud.controllers.ManagedServicesRequestController;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.api.config.ConfigurationService;
import io.javaoperatorsdk.operator.api.config.ControllerConfiguration;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import javax.inject.Inject;
import org.jboss.logging.Logger;

@QuarkusMain
public class RHOASOperator implements QuarkusApplication {

  @Inject KubernetesClient client;

  @Inject Operator operator;

  @Inject ConfigurationService configuration;

  @Inject ManagedKafkaConnectionController connectionController;

  @Inject ManagedServicesRequestController requestController;

  @Inject ManagedServiceAccountRequestController serviceAccountRequestController;

  private static final Logger LOG = Logger.getLogger(RHOASOperator.class);

  public static void main(String... args) {
    LOG.info("Autoupdating RHOAS Operator starting");
    Quarkus.run(RHOASOperator.class, args);
  }

  @Override
  public int run(String... args) throws Exception {
    ControllerConfiguration<?> config = configuration.getConfigurationFor(connectionController);
    LOG.info("CR class: " + config.getCustomResourceClass());

    config = configuration.getConfigurationFor(requestController);
    LOG.info("CR class: " + config.getCustomResourceClass());

    config = configuration.getConfigurationFor(serviceAccountRequestController);
    LOG.info("CR class: " + config.getCustomResourceClass());

    Quarkus.waitForExit();
    return 0;
  }
}
