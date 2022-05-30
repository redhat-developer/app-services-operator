package com.openshift.cloud.test;

import com.openshift.cloud.controllers.CloudServiceAccountRequestController;
import com.openshift.cloud.test.util.EmptyContext;
import com.openshift.cloud.test.util.MockAccessTokenSecretToolProfile;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequest;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequestBuilder;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequestSpecBuilder;
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
public class CloudServiceAccountRequestTest {

  @QuarkusKubernetesMockServer
  KubernetesServer server;

  @Inject
  CloudServiceAccountRequestController controller;

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
  public void testCSARGoldenScenario() {
    var csar = new CloudServiceAccountRequestBuilder()
        .withMetadata(new ObjectMetaBuilder().withGeneration(10l).withNamespace("test")
            .withName("csr-test").build())
        .withSpec(new CloudServiceAccountRequestSpecBuilder()
            .withAccessTokenSecretName("rh-managed-services-api-accesstoken")
            .withServiceAccountDescription("Sample Description")
            .withServiceAccountName("accountName").withServiceAccountSecretName("accountSecret")
            .build())
        .build();

    var result = controller.reconcile(csar,
        EmptyContext.emptyContext(CloudServiceAccountRequest.class));

    Assertions.assertTrue(result.isUpdateStatus());
  }
}
