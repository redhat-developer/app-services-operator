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

import io.fabric8.kubernetes.api.model.ObjectMeta;
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
        defaultClient.setBasePath("https://api.stage.openshift.com");
        
        // Configure HTTP bearer authorization: Bearer
        HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
        Bearer.setBearerToken("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICItNGVsY19WZE5fV3NPVVlmMkc0UXhyOEdjd0l4X0t0WFVDaXRhdExLbEx3In0.eyJleHAiOjE2MTA2NjExMTIsImlhdCI6MTYxMDY2MDIxMiwiYXV0aF90aW1lIjoxNjEwNjUyODQ1LCJqdGkiOiI2ZDk0M2JlMy0zZTBiLTRjOWUtYWEwYi0zZmJlMmZlMGI1OGQiLCJpc3MiOiJodHRwczovL3Nzby5yZWRoYXQuY29tL2F1dGgvcmVhbG1zL3JlZGhhdC1leHRlcm5hbCIsImF1ZCI6ImNsb3VkLXNlcnZpY2VzIiwic3ViIjoiZjo1MjhkNzZmZi1mNzA4LTQzZWQtOGNkNS1mZTE2ZjRmZTBjZTY6c3VwaXR0bWFfa2Fma2FfZGV2ZXhwIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY2xvdWQtc2VydmljZXMiLCJub25jZSI6IjQ2MDQ2Mjk5LTAwZTAtNDVmYS05N2ExLTQzM2Q4YWVkN2MzMyIsInNlc3Npb25fc3RhdGUiOiJhMmQ3YjgyNC05ZTA2LTRjYmMtYTNjMy1hMjEzZDc4NjVhYmMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vcHJvZC5mb28ucmVkaGF0LmNvbToxMzM3IiwiaHR0cHM6Ly9hcGkuY2xvdWQucmVkaGF0LmNvbSIsImh0dHBzOi8vcWFwcm9kYXV0aC5jbG91ZC5yZWRoYXQuY29tIiwiaHR0cHM6Ly9jbG91ZC5vcGVuc2hpZnQuY29tIiwiaHR0cHM6Ly9wcm9kLmZvby5yZWRoYXQuY29tIiwiaHR0cHM6Ly9jbG91ZC5yZWRoYXQuY29tIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJhdXRoZW50aWNhdGVkIiwiY2FuZGxlcGluX3N5c3RlbV9hY2Nlc3Nfdmlld19lZGl0X3BlcnNvbmFsIl19LCJzY29wZSI6Im9wZW5pZCIsImFjY291bnRfbnVtYmVyIjoiNjk1NDYwOCIsImlzX2ludGVybmFsIjpmYWxzZSwiYWNjb3VudF9pZCI6IjUzODU4NTM1IiwiaXNfYWN0aXZlIjp0cnVlLCJvcmdfaWQiOiIxMzgzODYxNCIsImxhc3RfbmFtZSI6IlBpdG1hbiIsInR5cGUiOiJVc2VyIiwibG9jYWxlIjoiZW5fVVMiLCJmaXJzdF9uYW1lIjoiU3VtbWVycyIsImVtYWlsIjoic3VwaXR0bWFAcmVkaGF0LmNvbSIsInVzZXJuYW1lIjoic3VwaXR0bWFfa2Fma2FfZGV2ZXhwIiwiaXNfb3JnX2FkbWluIjpmYWxzZX0.qeZn_6QUoOrFnTFlPRSEWozyMsiKtgWKMbB-LAQUvwDAjoQGeTMBEjJsTDE_t7E-7HuYMd1UH74k052Ef5K07vT65QlR_9HgyAIppGUFqg1_uZPPH_xQv2wcEp5AbqDaGvKzPrDpNhEvdaQ50H7AegurD7l7XKK5bw5-2876Py_i-lLMeVQbFdpG4UyMcv1v8gTo8TR9okoDYjezRMd5wr3Xxqd1hLD1uH6vCWmKOH4pjp-jegsSdN0dQA4l5CQG4fh9CiWYkB2KhW0C-_g1y-wTdx-Npugjp5RFs6uclViarEzCYZW9gc33DP1bzulHYbbJ_HSbps6p46m2pn1db_-Y0RqwzH28RM6WtutOtfmTULqt1C0zyhZ1ynF_1UaGbNLg8GkUrYbs5hIuP54lAGdMJPWX4qo2iX26IJS3X6nP4lf2xDY4e060LpEreuIUHkQRpXP7yVsnG2-EHPovukeTgtcFqQxt05PUils9oV3EVlAPRYW6hoCg3_AjV99Ztwl8mVlFeL-9mI0xbHL6ZGyU52vaLzlrYAzVVpOfkTbGe8v6AhJEXschtSBE019iBCvhV7byCzssoENMRRVPM5sbzt2mLI7zmgAGEwSva7nSJ3n5oSdYH-qh6YdTxizEPT9ggAZUAgyzF9qe4ts0acL4HPOKtEv4LurswSJetKc");
        
        this.controlPanelApiClient = new DefaultApi(defaultClient);

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
            
            // if (client
            // .inNamespace(resource.getMetadata().getNamespace())
            // .withName(resource.getMetadata().getName())
            // .get() != null) {
            //     client
            //     .inNamespace(resource.getMetadata().getNamespace())
            //     .withName(resource.getMetadata().getName())
            //     .delete();
            // }

            var newConnection = new ManagedKafkaConnection(resource.getSpec(), status);
            
            var meta = new ObjectMeta();
            meta.setName(resource.getMetadata().getName());
            meta.setNamespace(resource.getMetadata().getNamespace());
            meta.setLabels(resource.getMetadata().getLabels());

            newConnection.setMetadata(meta);

            client
                .inNamespace(resource.getMetadata().getNamespace())
                .withName(resource.getMetadata().getName())
                .createOrReplace(newConnection);

            return UpdateControl.noUpdate();
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
