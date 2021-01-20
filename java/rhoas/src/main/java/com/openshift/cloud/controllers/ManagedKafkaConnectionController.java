package com.openshift.cloud.controllers;

import com.openshift.cloud.ApiClient;
import com.openshift.cloud.ApiException;
import com.openshift.cloud.Configuration;
import com.openshift.cloud.api.DefaultApi;
import com.openshift.cloud.auth.HttpBearerAuth;
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

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class ManagedKafkaConnectionController implements ResourceController<ManagedKafkaConnection> {

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_PATTERN);
    private static final Logger LOG = Logger.getLogger(ManagedKafkaConnectionController.class.getName());
    private final KubernetesClient k8sClient;

    @ConfigProperty(name = "client.basePath", defaultValue = "https://api.stage.openshift.com")
    String clientBasePath;

    public ManagedKafkaConnectionController(KubernetesClient k8sClient) {
        this.k8sClient = k8sClient;
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

        try {
            var kafkaId = resource.getSpec().getKafkaId();
            var saSecretName = resource.getSpec().getCredentials().getServiceAccountSecretName();
            var namespace = resource.getMetadata().getNamespace();
            var saSecret = k8sClient.secrets().inNamespace(namespace).withName(saSecretName).get().getData().get("token");//TODO: what is the secret format?
            saSecret = new String(Base64.getDecoder().decode(saSecret));

            var kafkaServiceInfo = createClient(saSecret).getKafkaById(kafkaId);

            var bootStrapHost = kafkaServiceInfo.getBootstrapServerHost();
            var bootStrapServer = new BoostrapServer(bootStrapHost);

            var status = new ManagedKafkaConnectionStatus("Created", DATE_FORMATTER.format(new Date()), bootStrapServer, saSecretName);
            resource.setStatus(status);

            return UpdateControl.updateCustomResourceAndStatus(resource);
        } catch (ApiException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return UpdateControl.noUpdate();
        }

    }

    private DefaultApi createClient(String bearerToken) {

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath(clientBasePath);

        // Configure HTTP bearer authorization: Bearer
        HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
        Bearer.setBearerToken(bearerToken);

        return new DefaultApi(defaultClient);

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

}
