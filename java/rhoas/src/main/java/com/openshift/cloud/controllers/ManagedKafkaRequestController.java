package com.openshift.cloud.controllers;

import com.openshift.cloud.ApiClient;
import com.openshift.cloud.ApiException;
import com.openshift.cloud.Configuration;
import com.openshift.cloud.api.DefaultApi;
import com.openshift.cloud.auth.HttpBearerAuth;
import com.openshift.cloud.v1alpha.models.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.logging.Logger;

@Controller
public class ManagedKafkaRequestController  implements ResourceController<ManagedKafkaRequest> {

    private static final Logger LOG = Logger.getLogger(ManagedKafkaRequestController.class.getName());
    private final KubernetesClient k8sClient;

    @ConfigProperty(name = "client.basePath", defaultValue = "https://api.stage.openshift.com")
    String clientBasePath;

    public ManagedKafkaRequestController(KubernetesClient k8sClient) {
        this.k8sClient = k8sClient;
    }

    @Override
    public DeleteControl deleteResource(ManagedKafkaRequest managedKafkaRequest, Context<ManagedKafkaRequest> context) {
        LOG.info(String.format("Deleting resource %s", managedKafkaRequest.getMetadata().getName()));

        return DeleteControl.DEFAULT_DELETE;
    }

    @Override
    public UpdateControl<ManagedKafkaRequest> createOrUpdateResource(ManagedKafkaRequest resource, Context<ManagedKafkaRequest> context) {
        LOG.info(String.format("Update or create resource %s", resource.getMetadata().getName()));

        try {
            var saSecretName = resource.getSpec().getAccessTokenSecretName();
            var namespace = resource.getMetadata().getNamespace();
            var saSecret = k8sClient.secrets().inNamespace(namespace).withName(saSecretName).get().getStringData().get("token");//TODO: what is the secret format?
            var kafkaList = createClient(saSecret).listKafkas(null, null, null, null);

            var userKafkas = new HashMap<String, UserKafka>();

            kafkaList.getItems().stream().forEach(listItem -> {
                var userKafka = new UserKafka();
                userKafka.setId(listItem.getId());
                userKafka.setCreated(listItem.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME));
                userKafka.setOwner(listItem.getOwner());
                userKafka.setProvider(listItem.getCloudProvider());
                userKafka.setRegion(listItem.getRegion());

                userKafkas.put(listItem.getName(), userKafka);
            });

            var status = new ManagedKafkaRequestStatus(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), userKafkas);
            resource.setStatus(status);

            return UpdateControl.updateCustomResourceAndStatus(resource);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return UpdateControl.noUpdate();

    }


    private DefaultApi createClient(String bearerToken) {

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath(clientBasePath);

        // Configure HTTP bearer authorization: Bearer
        HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
        Bearer.setBearerToken(bearerToken);

        return new DefaultApi(defaultClient);

    }


    @Override
    public void init(EventSourceManager eventSourceManager) {
        LOG.info("Init! This is where we would add watches for child resources");
    }


    private MixedOperation<ManagedKafkaRequest, ManagedKafkaRequestList, Resource<ManagedKafkaRequest>> getManagedKafkaRequestClient(
            KubernetesClient client) {
        // Create Custom Resource Context
        var context = CustomResourceDefinitionContext.fromCrd(CustomResourceDefinitionContext.v1beta1CRDFromCustomResourceType(ManagedKafkaRequest.class).build());
        return client.customResources(context, ManagedKafkaRequest.class, ManagedKafkaRequestList.class);

    }

}
