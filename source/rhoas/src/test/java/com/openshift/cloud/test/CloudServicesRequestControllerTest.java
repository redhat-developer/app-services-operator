package com.openshift.cloud.test;

import com.openshift.cloud.controllers.CloudServicesRequestController;
import com.openshift.cloud.test.util.EmptyContext;
import com.openshift.cloud.v1alpha.models.CloudServicesRequest;
import com.openshift.cloud.v1alpha.models.CloudServicesRequestBuilder;
import com.openshift.cloud.v1alpha.models.CloudServicesRequestSpec;
import com.openshift.cloud.v1alpha.models.UserKafka;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.bf2.test.mock.QuarkusKubeMockServer;
import org.bf2.test.mock.QuarkusKubernetesMockServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

@QuarkusTestResource(QuarkusKubeMockServer.class)
@QuarkusTest
public class CloudServicesRequestControllerTest {

    @QuarkusKubernetesMockServer
    static KubernetesServer server;

    @Inject
    CloudServicesRequestController controller;

    @Test
    public void testCloudServicesRequest() {
        var cloudServicesRequest = new CloudServicesRequestBuilder()
                .withMetadata(new ObjectMetaBuilder()
                        .withNamespace("test")
                        .withName("csr-test")
                        .build()
                )
                .withSpec(new CloudServicesRequestSpec("rh-managed-services-api-accesstoken"))
                .build();

        var result = controller.createOrUpdateResource(cloudServicesRequest, EmptyContext.EMPTY_CONTEXT);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getCustomResource());
        Assertions.assertNotNull(result.getCustomResource().getStatus());

        List<UserKafka> userKafkas = ((CloudServicesRequest)result.getCustomResource()).getStatus().getUserKafkas();
        Assertions.assertNotNull(userKafkas);
        Assertions.assertEquals("1234567890", userKafkas.get(0).getId());

    }
}
