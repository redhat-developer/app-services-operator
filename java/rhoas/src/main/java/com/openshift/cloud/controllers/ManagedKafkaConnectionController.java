package com.openshift.cloud.controllers;

import com.openshift.cloud.ApiClient;
import com.openshift.cloud.ApiException;
import com.openshift.cloud.Configuration;
import com.openshift.cloud.api.DefaultApi;
import com.openshift.cloud.auth.HttpBearerAuth;
import com.openshift.cloud.beans.ApiClients;
import com.openshift.cloud.v1alpha.models.BoostrapServer;
import com.openshift.cloud.v1alpha.models.ManagedKafkaConnection;
import com.openshift.cloud.v1alpha.models.ManagedKafkaConnectionList;
import com.openshift.cloud.v1alpha.models.ManagedKafkaConnectionStatus;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.javaoperatorsdk.operator.api.*;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class ManagedKafkaConnectionController implements ResourceController<ManagedKafkaConnection> {

    private static final Logger LOG = Logger.getLogger(ManagedKafkaConnectionController.class.getName());
    private DefaultApi controlPanelApiClient;

    @ConfigProperty(name = "client.basePath", defaultValue = "https://api.stage.openshift.com")
    String clientBasePath;

    @ConfigProperty(name = "client.bearerToken")
    String clientBearerToken;


    public ManagedKafkaConnectionController() {
    }

    @Override
    public DeleteControl deleteResource(ManagedKafkaConnection resource, Context<ManagedKafkaConnection> context) {
        LOG.info(String.format("Deleting resource %s", resource.getMetadata().getName()));

        return DeleteControl.DEFAULT_DELETE;
    }

    @Override
    public UpdateControl<ManagedKafkaConnection> createOrUpdateResource(ManagedKafkaConnection resource,
                                                                        Context<ManagedKafkaConnection> context) {
        LOG.info(String.format("Creating or Updating resource %s", resource.getMetadata().getName()));

        var kafkaId = resource.getSpec().getKafkaId();

        try {
            var kafkaServiceInfo = controlPanelApiClient.getKafkaById(kafkaId);
            var bootStrapHost = kafkaServiceInfo.getBootstrapServerHost();
            var bootStrapServer = new BoostrapServer(bootStrapHost);

            var status = new ManagedKafkaConnectionStatus("Created", "Updated", bootStrapServer, "sa secretname");
            resource.setStatus(status);

            return UpdateControl.updateCustomResourceAndStatus(resource);
        } catch (ApiException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return UpdateControl.noUpdate();
        }

    }

    /**
     * client creation is handled after object construction because the
     * client depends on injected values. This method is called by quarkus.
     */
    @PostConstruct
    public void createClient() {

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath(clientBasePath);

        // Configure HTTP bearer authorization: Bearer
        HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
        Bearer.setBearerToken(clientBearerToken);

        this.controlPanelApiClient = new DefaultApi(defaultClient);

    }


    /**
     * In init typically you might want to register event sources.
     *
     * @param eventSourceManager
     */
    @Override
    public void init(EventSourceManager eventSourceManager) {
        LOG.info("Init! This is where we would add watches for child resources");


    }

    private MixedOperation<ManagedKafkaConnection, ManagedKafkaConnectionList, Resource<ManagedKafkaConnection>> getKafkaUserClient(
            KubernetesClient client) {
        // Create Custom Resource Context
        var context = CustomResourceDefinitionContext.v1beta1CRDFromCustomResourceType(ManagedKafkaConnection.class).build();

        return client.customResources(context, ManagedKafkaConnection.class, ManagedKafkaConnectionList.class);

    }
}
