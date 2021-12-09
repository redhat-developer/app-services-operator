package com.openshift.cloud;

import com.openshift.cloud.beans.KafkaK8sClients;
import com.openshift.cloud.controllers.CloudServiceAccountRequestController;
import com.openshift.cloud.controllers.CloudServicesRequestController;
import com.openshift.cloud.controllers.KafkaConnectionController;
import com.openshift.cloud.controllers.ServiceRegistryConnectionController;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.api.config.ConfigurationService;
import io.javaoperatorsdk.operator.api.config.ControllerConfiguration;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.ArrayList;

import javax.inject.Inject;

@QuarkusMain
public class RHOASOperator implements QuarkusApplication {

  @Inject
  Operator operator;

  @Inject
  ConfigurationService configuration;

  @Inject
  KafkaConnectionController connectionController;

  @Inject
  ServiceRegistryConnectionController serviceRegistryConnectionController;

  @Inject
  CloudServicesRequestController requestController;

  @Inject
  CloudServiceAccountRequestController serviceAccountRequestController;

  @Inject
  KafkaK8sClients client;

  @ConfigProperty(name = "rhoas.client.apiBasePath")
  String clientBasePath;

  private static final Logger LOG = Logger.getLogger(RHOASOperator.class);

  public static void main(String... args) {
    LOG.info("RHOAS Operator starting");
    Quarkus.run(RHOASOperator.class, args);
  }

  @Override
  public int run(String... args) throws Exception {

    client.initQuickStarts();

    LOG.info("Using API URL: " + clientBasePath);

    ControllerConfiguration<?> config = configuration.getConfigurationFor(connectionController);
    LOG.info("CR class: " + config.getCustomResourceClass());

    config = configuration.getConfigurationFor(requestController);
    LOG.info("CR class: " + config.getCustomResourceClass());

    config = configuration.getConfigurationFor(serviceAccountRequestController);
    LOG.info("CR class: " + config.getCustomResourceClass());

    config = configuration.getConfigurationFor(serviceRegistryConnectionController);
    LOG.info("CR class: " + config.getCustomResourceClass());

    cleanupDeletedLegacyFinalizers();

    operator.start();

    Quarkus.waitForExit();
    return 0;
  }

  private void cleanupDeletedLegacyFinalizers() {

    // Clean up legacy Finalizers for deleted resources
    // We removed finalizers for several types, but didn't correctly remove them from instanced
    // resources in k8s
    // if those resources were deleted then we need to remove the finalizers here outside of the
    // normal reconsiliation loop
    var srcs = new ArrayList<>(client.serviceRegistryConnection().list().getItems());
    srcs.removeIf(item -> !item.isMarkedForDeletion());
    srcs.forEach(
        src -> src.removeFinalizer("serviceregistryconnections.rhoas.redhat.com/finalizer"));
    srcs.forEach(src -> client.serviceRegistryConnection().createOrReplace(src));

    var csrs = new ArrayList<>(client.cloudServicesRequest().list().getItems());
    csrs.removeIf(item -> !item.isMarkedForDeletion());
    csrs.forEach(csr -> csr.removeFinalizer("cloudservicesrequests.rhoas.redhat.com/finalizer"));
    csrs.forEach(csr -> client.cloudServicesRequest().createOrReplace(csr));

    var akcs = new ArrayList<>(client.kafkaConnection().list().getItems());
    akcs.removeIf(item -> !item.isMarkedForDeletion());
    akcs.forEach(akc -> akc.removeFinalizer("kafkaconnections.rhoas.redhat.com/finalizer"));
    akcs.forEach(akc -> client.kafkaConnection().createOrReplace(akc));

    var csars = new ArrayList<>(client.cloudServiceAccountRequest().list().getItems());
    csars.removeIf(item -> !item.isMarkedForDeletion());
    csars.forEach(
        csar -> csar.removeFinalizer("cloudserviceaccountrequests.rhoas.redhat.com/finalizer"));
    csars.forEach(csar -> client.cloudServiceAccountRequest().createOrReplace(csar));


  }
}
