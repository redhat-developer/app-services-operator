package com.openshift.cloud;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.config.runtime.DefaultConfigurationService;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

public class RHOASOperator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RHOASOperator.class);


  public static void main(String[] args) {
    LOGGER.info("RHOAS Operator starting");

    KubernetesClient client = new DefaultKubernetesClient();
    Operator operator = new Operator(client, DefaultConfigurationService.instance());
    // operator.registerController(new CustomServiceController(client));
  }
}
