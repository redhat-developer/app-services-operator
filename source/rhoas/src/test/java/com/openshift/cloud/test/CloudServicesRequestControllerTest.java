package com.openshift.cloud.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.openshift.cloud.api.kas.models.KafkaRequest;
import com.openshift.cloud.beans.MockKafkaApiClient;
import com.openshift.cloud.controllers.CloudServicesRequestController;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.test.util.EmptyContext;
import com.openshift.cloud.test.util.MockAccessTokenSecretToolProfile;
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
import io.quarkus.test.junit.TestProfile;
import java.net.HttpURLConnection;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.bf2.test.mock.QuarkusKubeMockServer;
import org.bf2.test.mock.QuarkusKubernetesMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTestResource(QuarkusKubeMockServer.class)
@TestProfile(MockAccessTokenSecretToolProfile.class)
@QuarkusTest
public class CloudServicesRequestControllerTest {

  @QuarkusKubernetesMockServer KubernetesServer server;

  @Inject CloudServicesRequestController controller;
  @Inject MockKafkaApiClient mockKafkaApiClient;

  /** Adds a secret to the k8s mock server */
  @BeforeEach
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

  @AfterEach
  public void resetMocks() {
    mockKafkaApiClient.resetKafkas();
  }

  @Test
  public void testCloudServicesRequest() {
    var cloudServicesRequest =
        new CloudServicesRequestBuilder()
            .withMetadata(
                new ObjectMetaBuilder()
                    .withGeneration(10l)
                    .withNamespace("test")
                    .withName("csr-test")
                    .build())
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

  @Test
  /**
   * This test gets a userKafkaRequest, updates the userKafkaObject in the mock, and runs the get
   * again. Before https://github.com/bf2fc6cc711aee1a0c2a/operator/issues/151 is fixed, this will
   * fail as the updates are not correctly delivered. However, if this test passes then #151 is
   * likely fixed.
   */
  public void userKafkasUpdate() {
    var cloudServicesRequest =
        new CloudServicesRequestBuilder()
            .withMetadata(
                new ObjectMetaBuilder()
                    .withGeneration(10l)
                    .withNamespace("test")
                    .withName("csr-test")
                    .build())
            .withSpec(new CloudServicesRequestSpec("rh-managed-services-api-accesstoken"))
            .build();

    var result =
        controller.createOrUpdateResource(
            cloudServicesRequest, EmptyContext.emptyContext(CloudServicesRequest.class));

    UserKafka userKafkas = (result.getCustomResource()).getStatus().getUserKafkas().get(0);

    // change the default kafka status from "status" to "ready"
    changeDefaultKafkaInMock();

    cloudServicesRequest.getMetadata().setGeneration(11l);

    result =
        controller.createOrUpdateResource(
            cloudServicesRequest, EmptyContext.emptyContext(CloudServicesRequest.class));

    UserKafka userKafkas2 = (result.getCustomResource()).getStatus().getUserKafkas().get(0);
    assertEquals("status", userKafkas.getStatus());
    assertEquals("ready", userKafkas2.getStatus());
  }

  private void changeDefaultKafkaInMock() {
    var request = new KafkaRequest();
    request.bootstrapServerHost("testHost");
    request.cloudProvider("cloudProvider");
    request.createdAt(OffsetDateTime.now());
    request.name("name");
    request.owner("owner");
    request.setId("1234567890");
    request.status("ready");
    request.setUpdatedAt(OffsetDateTime.now());
    request.setRegion("region");

    mockKafkaApiClient.addKafka(request);
  }
}
