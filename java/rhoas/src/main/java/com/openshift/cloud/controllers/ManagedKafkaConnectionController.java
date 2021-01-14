package com.openshift.cloud.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;

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
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.DeleteControl;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;
import io.javaoperatorsdk.operator.processing.event.EventSourceManager;

@Controller
public class ManagedKafkaConnectionController implements ResourceController<ManagedKafkaConnection> {

    private static final Logger LOG = Logger.getLogger(ManagedKafkaConnectionController.class.getName());
    private ApiClients k8sClients;
    private DefaultApi controlPanelApiClient;

    public ManagedKafkaConnectionController(ApiClients clients) {
        this.k8sClients = clients;
        
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://api.openshift.com");
        
        // Configure HTTP bearer authorization: Bearer
        HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
        Bearer.setBearerToken("BEARER TOKEN");
        
        DefaultApi controlPanelApiClient = new DefaultApi(defaultClient);

    }

    @Override
    public DeleteControl deleteResource(ManagedKafkaConnection resource, Context<ManagedKafkaConnection> context) {
        LOG.info(String.format("Deleting resource %s", resource.getMetadata().getName()));

        // client.
        // .services()
        // .inNamespace(resource.getMetadata().getNamespace())
        // .withName(resource.getSpec().getName())
        // .delete();
        return DeleteControl.DEFAULT_DELETE;
    }

    @Override
    public UpdateControl<ManagedKafkaConnection> createOrUpdateResource(ManagedKafkaConnection resource,
            Context<ManagedKafkaConnection> context) {
        LOG.info(String.format("Creating or Updating resource %s", resource.getMetadata().getName()));

        var client = this.k8sClients.managedKafkaConnection();
        var kafkaId = resource.getSpec().getKafkaId();

        try {
            var kafkaServiceInfo = controlPanelApiClient.getKafkaById(kafkaId);
            var bootStrapHost = kafkaServiceInfo.getBootstrapServerHost();
            var bootStrapServer = new BoostrapServer(bootStrapHost);

            var status = new ManagedKafkaConnectionStatus("Created", "Updated", bootStrapServer, "sa secretname");
            resource.setStatus(status);

            client
                .inNamespace(resource.getMetadata().getNamespace())
                .createOrReplace(resource);

            return UpdateControl.updateCustomResource(resource);
        } catch (ApiException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return UpdateControl.noUpdate();    
        }

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
