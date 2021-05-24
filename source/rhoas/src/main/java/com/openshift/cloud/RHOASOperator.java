package com.openshift.cloud;

import com.openshift.cloud.controllers.CloudServiceAccountRequestController;
import com.openshift.cloud.controllers.CloudServicesRequestController;
import com.openshift.cloud.controllers.KafkaConnectionController;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.api.config.ConfigurationService;
import io.javaoperatorsdk.operator.api.config.ControllerConfiguration;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@QuarkusMain
public class RHOASOperator implements QuarkusApplication {

  @Inject
  Operator operator;

  @Inject
  ConfigurationService configuration;

  @Inject
  KafkaConnectionController connectionController;

  @Inject
  CloudServicesRequestController requestController;

  @Inject
  CloudServiceAccountRequestController serviceAccountRequestController;

  @ConfigProperty(name = "rhoas.client.apiBasePath")
  String clientBasePath;

  private static final Logger LOG = Logger.getLogger(RHOASOperator.class);

  public static void main(String... args) {
    LOG.info("RHOAS Operator starting");
    Quarkus.run(RHOASOperator.class, args);
  }

  @Override
  public int run(String... args) throws Exception {
    LOG.info("Using API URL: " + clientBasePath);

    ControllerConfiguration<?> config = configuration.getConfigurationFor(connectionController);
    LOG.info("CR class: " + config.getCustomResourceClass());

    config = configuration.getConfigurationFor(requestController);
    LOG.info("CR class: " + config.getCustomResourceClass());

    config = configuration.getConfigurationFor(serviceAccountRequestController);
    LOG.info("CR class: " + config.getCustomResourceClass());

    operator.start();

    Quarkus.waitForExit();
    return 0;
  }
}
