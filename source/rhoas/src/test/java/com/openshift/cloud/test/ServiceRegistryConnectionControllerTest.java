package com.openshift.cloud.test;

import com.openshift.cloud.controllers.AbstractCloudServicesController;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.controllers.KafkaConnectionController;
import com.openshift.cloud.controllers.ServiceRegistryConnectionController;
import com.openshift.cloud.test.util.EmptyContext;
import com.openshift.cloud.test.util.MockAccessTokenSecretToolProfile;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition.Status;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition.Type;
import com.openshift.cloud.v1alpha.models.Credentials;
import com.openshift.cloud.v1alpha.models.KafkaConnection;
import com.openshift.cloud.v1alpha.models.KafkaConnectionBuilder;
import com.openshift.cloud.v1alpha.models.KafkaConnectionSpecBuilder;
import com.openshift.cloud.v1alpha.models.ServiceRegistryConnection;
import com.openshift.cloud.v1alpha.models.ServiceRegistryConnectionBuilder;
import com.openshift.cloud.v1alpha.models.ServiceRegistryConnectionSpecBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.bf2.test.mock.QuarkusKubeMockServer;
import org.bf2.test.mock.QuarkusKubernetesMockServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.net.HttpURLConnection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTestResource(QuarkusKubeMockServer.class)
@TestProfile(MockAccessTokenSecretToolProfile.class)
@QuarkusTest
public class ServiceRegistryConnectionControllerTest {

  @QuarkusKubernetesMockServer
  KubernetesServer server;

  @Inject
  ServiceRegistryConnectionController controller;

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
  public void testRegistryControllerEnforcesRequiredLabels() {
    var registryConnection = new ServiceRegistryConnectionBuilder()
        .withMetadata(new ObjectMetaBuilder().withGeneration(10l).withNamespace("test")
            .withName("kc-test").build())
        .withSpec(new ServiceRegistryConnectionSpecBuilder()
            .withAccessTokenSecretName("rh-managed-services-api-accesstoken")
            .withCredentials(new Credentials("sa-secret")).withServiceRegistryId("1234567890")
            .build())
        .build();
    var result = controller.reconcile(registryConnection,
        EmptyContext.emptyContext(ServiceRegistryConnection.class));

    var labels = result.getResource().getMetadata().getLabels();

    assertEquals(AbstractCloudServicesController.COMPONENT_LABEL_VALUE,
        labels.get(AbstractCloudServicesController.COMPONENT_LABEL_KEY));
    assertEquals(AbstractCloudServicesController.MANAGED_BY_LABEL_VALUE,
        labels.get(AbstractCloudServicesController.MANAGED_BY_LABEL_KEY));

  }

  @Test
  public void testKafkaConnectionRequest() {
    var registryConnection = new ServiceRegistryConnectionBuilder()
        .withMetadata(new ObjectMetaBuilder().withGeneration(10l).withNamespace("test")
            .withName("kc-test").build())
        .withSpec(new ServiceRegistryConnectionSpecBuilder()
            .withAccessTokenSecretName("rh-managed-services-api-accesstoken")
            .withCredentials(new Credentials("sa-secret")).withServiceRegistryId("1234567890")
            .build())
        .build();

    var result = controller.reconcile(registryConnection,
        EmptyContext.emptyContext(ServiceRegistryConnection.class));
    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getResource());
    Assertions.assertNotNull(result.getResource().getStatus());

    var status = result.getResource().getStatus();

    CloudServiceCondition condition =
        ConditionUtil.getCondition(status.getConditions(), Type.AcccesTokenSecretValid);
    assertEquals(Status.True, condition.getStatus());

    assertEquals("null/apis/registry/v2", status.getRegistryUrl());
    assertEquals("sa-secret", status.getServiceAccountSecretName());

    var metaData = status.getMetadata();
    assertEquals("rhoas", metaData.get("provider"));
    assertEquals("serviceregistry", metaData.get("type"));
    assertEquals(
        "https://identity.api.openshift.com/auth/realms/rhoas/protocol/openid-connect/token",
        metaData.get("oauthTokenUrl"));
  }
}
