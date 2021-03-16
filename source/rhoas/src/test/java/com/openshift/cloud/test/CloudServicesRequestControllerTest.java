package com.openshift.cloud.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.openshift.cloud.controllers.CloudServicesRequestController;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.test.util.EmptyContext;
import com.openshift.cloud.v1alpha.models.CloudServicesRequest;
import com.openshift.cloud.v1alpha.models.CloudServicesRequestBuilder;
import com.openshift.cloud.v1alpha.models.CloudServicesRequestSpec;
import com.openshift.cloud.v1alpha.models.CloudServicesRequestStatus;
import com.openshift.cloud.v1alpha.models.KafkaCondition.Status;
import com.openshift.cloud.v1alpha.models.KafkaCondition.Type;
import com.openshift.cloud.v1alpha.models.UserKafka;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.bf2.test.mock.QuarkusKubeMockServer;
import org.bf2.test.mock.QuarkusKubernetesMockServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@QuarkusTestResource(QuarkusKubeMockServer.class)
@QuarkusTest
public class CloudServicesRequestControllerTest {

  @QuarkusKubernetesMockServer static KubernetesServer server;

  @Inject CloudServicesRequestController controller;

  /** Adds a secret to the k8s mock server */
  @BeforeAll
  public void setSecret() {
    var secret =
        new SecretBuilder()
            .withNewMetadata()
            .withNamespace("test")
            .withName("rh-managed-services-api-accesstoken")
            .endMetadata()
            .addToData(Map.of("value", "bXl0b2tlbg=="));

    server
        .expect()
        .get()
        .withPath("/api/v1/namespaces/test/secrets/rh-managed-services-api-accesstoken")
        .andReturn(HttpURLConnection.HTTP_OK, secret.build())
        .once();
  }

  @Test
  public void testCloudServicesRequest() {
    var cloudServicesRequest =
        new CloudServicesRequestBuilder()
            .withMetadata(
                new ObjectMetaBuilder().withNamespace("test").withName("csr-test").build())
            .withSpec(new CloudServicesRequestSpec("rh-managed-services-api-accesstoken"))
            .build();

    var result =
        controller.createOrUpdateResource(
            cloudServicesRequest, EmptyContext.emptyContext(CloudServicesRequest.class));

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getCustomResource());
    Assertions.assertNotNull(result.getCustomResource().getStatus());

    CloudServicesRequestStatus status =
        (CloudServicesRequestStatus) result.getCustomResource().getStatus();

    var condition = ConditionUtil.getCondition(status.getConditions(), Type.AcccesTokenSecretValid);
    assertEquals(Status.True, condition.getStatus());

    List<UserKafka> userKafkas =
        ((CloudServicesRequest) result.getCustomResource()).getStatus().getUserKafkas();
    Assertions.assertNotNull(userKafkas);
    Assertions.assertEquals("1234567890", userKafkas.get(0).getId());
  }
}
