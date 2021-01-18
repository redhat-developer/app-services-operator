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
        Bearer.setBearerToken("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICItNGVsY19WZE5fV3NPVVlmMkc0UXhyOEdjd0l4X0t0WFVDaXRhdExLbEx3In0.eyJleHAiOjE2MTA5ODM4MDIsImlhdCI6MTYxMDk4MjkwMiwiYXV0aF90aW1lIjoxNjEwOTgyODg3LCJqdGkiOiI2NTY1MTA2YS1mMTVmLTQ3ZWItOTdkNy0wMWMyOWFkYmJmOGUiLCJpc3MiOiJodHRwczovL3Nzby5yZWRoYXQuY29tL2F1dGgvcmVhbG1zL3JlZGhhdC1leHRlcm5hbCIsImF1ZCI6ImNsb3VkLXNlcnZpY2VzIiwic3ViIjoiZjo1MjhkNzZmZi1mNzA4LTQzZWQtOGNkNS1mZTE2ZjRmZTBjZTY6c3VwaXR0bWFfa2Fma2FfZGV2ZXhwIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY2xvdWQtc2VydmljZXMiLCJub25jZSI6ImZiNDRlNzhlLTk2MmEtNDRkMi04MjZkLWRhZDczNmM4MWJmYSIsInNlc3Npb25fc3RhdGUiOiJiOWVhNjdhZC1kN2ExLTQ3MGQtYTRjYi0xMmRlNzMxNjE0OGMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vcHJvZC5mb28ucmVkaGF0LmNvbToxMzM3IiwiaHR0cHM6Ly9hcGkuY2xvdWQucmVkaGF0LmNvbSIsImh0dHBzOi8vcWFwcm9kYXV0aC5jbG91ZC5yZWRoYXQuY29tIiwiaHR0cHM6Ly9jbG91ZC5vcGVuc2hpZnQuY29tIiwiaHR0cHM6Ly9wcm9kLmZvby5yZWRoYXQuY29tIiwiaHR0cHM6Ly9jbG91ZC5yZWRoYXQuY29tIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJhdXRoZW50aWNhdGVkIiwiY2FuZGxlcGluX3N5c3RlbV9hY2Nlc3Nfdmlld19lZGl0X3BlcnNvbmFsIl19LCJzY29wZSI6Im9wZW5pZCIsImFjY291bnRfbnVtYmVyIjoiNjk1NDYwOCIsImlzX2ludGVybmFsIjpmYWxzZSwiYWNjb3VudF9pZCI6IjUzODU4NTM1IiwiaXNfYWN0aXZlIjp0cnVlLCJvcmdfaWQiOiIxMzgzODYxNCIsImxhc3RfbmFtZSI6IlBpdG1hbiIsInR5cGUiOiJVc2VyIiwibG9jYWxlIjoiZW5fVVMiLCJmaXJzdF9uYW1lIjoiU3VtbWVycyIsImVtYWlsIjoic3VwaXR0bWFAcmVkaGF0LmNvbSIsInVzZXJuYW1lIjoic3VwaXR0bWFfa2Fma2FfZGV2ZXhwIiwiaXNfb3JnX2FkbWluIjpmYWxzZX0.u7y6zS-w17hUBDqtfsdVIRKMUk-C5u3eevcknnHxP6YK09WaAlF0MMYjVP-9S1DBCZVxhlfN-kKUf01Nqt0NFPnueOcUHkJpd8GyC8FYN7NGaW-PWp0su3H4KLvHruhy54cSChO59UHTq2oeX4toCUJelvz39DO95JI3a_OkGORfpLV_s9aV12_zLoNnJ_1pNL7VaZvH_uCiPeTk5p0YscY7gtybHtqleOBx7agWVRcnXu9Wg67qAD3_THzzQTrj_aprG0YCMWyePV3gZcV6aAQpmR4POdBUEK36opRITy2_QC-VLeYtAxtv5z0vLaqtvGtSB7yDV3hBGPGWxV6f4D7n2z8377McJUuk-Phnxu0LZW2xqt5B_JfdfBJ67KIQUYBe2SsL39JXTfA0ZM8XTvAaIRsYQXfXDt5o_pE7g4wIx8ddhftCsVSTvWtu0MjZObWj_wnjfU4PJtq1xyhncyjvcn5P8dCAcyk3OFqEYf6W79tz7uZzkjYJoEoqKJkDSc5yRe7guDWXU4l4Mi6MUtveFtULcnoHqkOMq-4RyPjB3Jq4iE770UQv_1pm_RvlBK9wQOh5V1U1eBIZS5Ui7BgNI__iVdmE6e91HWECJNFMihzNE7SiS8vaBIAs5akiI-nP0n2ZG1mSw1zifoS4BBx8ieiDiixfDkjRjzWfXc");

        this.controlPanelApiClient = new DefaultApi(defaultClient);

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

        var client = this.k8sClients.managedKafkaConnection();
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
