package com.openshift.cloud.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.openshift.cloud.v1alpha.models.KafkaConnection.*;

import com.openshift.cloud.controllers.AbstractCloudServicesController;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.controllers.KafkaConnectionController;
import com.openshift.cloud.test.util.EmptyContext;
import com.openshift.cloud.test.util.MockAccessTokenSecretToolProfile;
import com.openshift.cloud.v1alpha.models.Credentials;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition.Status;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition.Type;
import com.openshift.cloud.v1alpha.models.KafkaConnection;
import com.openshift.cloud.v1alpha.models.KafkaConnectionBuilder;
import com.openshift.cloud.v1alpha.models.KafkaConnectionSpecBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import java.net.HttpURLConnection;
import java.util.Map;
import javax.inject.Inject;
import org.bf2.test.mock.QuarkusKubeMockServer;
import org.bf2.test.mock.QuarkusKubernetesMockServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTestResource(QuarkusKubeMockServer.class)
@TestProfile(MockAccessTokenSecretToolProfile.class)
@QuarkusTest
public class KafkaConnectionControllerTest {

  @QuarkusKubernetesMockServer
  KubernetesServer server;

  @Inject
  KafkaConnectionController controller;

  /** Adds a secret to the k8s mock server */
  @BeforeEach
  public void setSecret() {
    var secret = new SecretBuilder().withNewMetadata().withNamespace("test")
        .withName("rh-managed-services-api-accesstoken").endMetadata()
        .addToData(Map.of("value", "bXl0b2tlbg=="));

    server.expect().get()
        .withPath("/api/v1/namespaces/test/secrets/rh-managed-services-api-accesstoken")
        .andReturn(HttpURLConnection.HTTP_OK, secret.build()).once();
  }

  @Test
  public void testKafkaControllerEnforcesRequiredLabels() {
    var kafkaConnectionRequest = new KafkaConnectionBuilder()
        .withMetadata(new ObjectMetaBuilder().withGeneration(10l).withNamespace("test")
            .withName("kc-test").build())
        .withSpec(new KafkaConnectionSpecBuilder()
            .withAccessTokenSecretName("rh-managed-services-api-accesstoken")
            .withCredentials(new Credentials("sa-secret")).withKafkaId("1234567890").build())
        .build();
    var result = controller.reconcile(kafkaConnectionRequest,
        EmptyContext.emptyContext(KafkaConnection.class));

    var labels = ((KafkaConnection) result.getResource()).getMetadata().getLabels();

    assertEquals(AbstractCloudServicesController.COMPONENT_LABEL_VALUE,
        labels.get(AbstractCloudServicesController.COMPONENT_LABEL_KEY));
    assertEquals(AbstractCloudServicesController.MANAGED_BY_LABEL_VALUE,
        labels.get(AbstractCloudServicesController.MANAGED_BY_LABEL_KEY));

  }

  // TODO create issue to cover the test
  @Test
  public void testKafkaConnectionRequest() {
    var kafkaConnectionRequest = new KafkaConnectionBuilder()
        .withMetadata(new ObjectMetaBuilder().withGeneration(10l).withNamespace("test")
            .withLabels(Map.of(AbstractCloudServicesController.COMPONENT_LABEL_KEY,
                AbstractCloudServicesController.COMPONENT_LABEL_VALUE,
                AbstractCloudServicesController.MANAGED_BY_LABEL_KEY,
                AbstractCloudServicesController.MANAGED_BY_LABEL_VALUE))
            .withName("kc-test").build())
        .withSpec(new KafkaConnectionSpecBuilder()
            .withAccessTokenSecretName("rh-managed-services-api-accesstoken")
            .withCredentials(new Credentials("sa-secret")).withKafkaId("1234567890").build())
        .build();
    var result = controller.reconcile(kafkaConnectionRequest,
        EmptyContext.emptyContext(KafkaConnection.class));

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getResource());
    Assertions.assertNotNull(result.getResource().getStatus());

    var status = ((KafkaConnection) result.getResource()).getStatus();

    CloudServiceCondition condition =
        ConditionUtil.getCondition(status.getConditions(), Type.AcccesTokenSecretValid);
    assertEquals(Status.True, condition.getStatus());

    assertEquals("testHost", status.getBootstrapServerHost());
    assertEquals("sa-secret", status.getServiceAccountSecretName());

    var metaData = status.getMetadata();
    assertEquals("PLAIN", metaData.get("saslMechanism"));
    assertEquals("SASL_SSL", metaData.get("securityProtocol"));
    assertEquals("https://console.redhat.com/beta/application-services/streams/kafkas/1234567890",
        metaData.get("cloudUI"));
    assertEquals("rhoas", metaData.get("provider"));
    assertEquals("kafka", metaData.get("type"));
  }
}
