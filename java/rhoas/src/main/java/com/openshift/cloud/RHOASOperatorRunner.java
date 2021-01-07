package com.openshift.cloud;

import com.openshift.cloud.controllers.ManagedKafkaController;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.Operator;

public class RHOASOperatorRunner {

  public static void main(String[] args) {
    KubernetesClient client = new DefaultKubernetesClient();
    Operator operator = new Operator(client, null);
    operator.registerController(new ManagedKafkaController(client));
  }
}
