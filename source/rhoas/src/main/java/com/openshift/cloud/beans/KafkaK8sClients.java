package com.openshift.cloud.beans;

import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequest;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequestList;
import com.openshift.cloud.v1alpha.models.CloudServicesRequest;
import com.openshift.cloud.v1alpha.models.CloudServicesRequestList;
import com.openshift.cloud.v1alpha.models.KafkaConnection;
import com.openshift.cloud.v1alpha.models.KafkaConnectionList;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ListOptions;
import io.fabric8.kubernetes.api.model.ListOptionsBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.V1beta1ApiextensionAPIGroupDSL;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.internal.KubernetesDeserializer;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import io.fabric8.openshift.client.OpenShiftClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.jboss.logging.Logger;

/**
 * This class is processed at startup and checks that CRDs are installed and provides apiClients for
 * use
 */
@ApplicationScoped
public final class KafkaK8sClients {

  private static final String CONNECT_QUICKSTART =
      "/olm/quickstarts/rhosak-openshift-connect-quickstart.yaml";
  private static final String GETTING_STARTED_QUICKSTART =
      "/olm/quickstarts/rhosak-openshift-getting-started-quickstart.yaml";
  private static final String KAFKACAT_QUICKSTART =
      "/olm/quickstarts/rhosak-openshift-kafkacat-quickstart.yaml";
  private static final String QUARKUS_BIND_QUICKSTART =
      "/olm/quickstarts/rhosak-openshift-quarkus-bind-quickstart.yaml";

  private static final String[] QUICKSTARTS = {CONNECT_QUICKSTART, GETTING_STARTED_QUICKSTART,
      KAFKACAT_QUICKSTART, QUARKUS_BIND_QUICKSTART};

  private static final Logger LOG = Logger.getLogger(KafkaK8sClients.class.getName());

  @Inject
  KubernetesClient client;

  private CustomResourceDefinition akcCrd;
  private CustomResourceDefinition cscrCrd;
  private CustomResourceDefinition csarCrd;

  @PostConstruct
  public void init() {
    LOG.info("Init");

    var crds = client.apiextensions().v1beta1();

    this.akcCrd = initKafkaConnectionCRDAndClient(crds);
    this.cscrCrd = initCloudServicesRequestCRDAndClient(crds);
    this.csarCrd = initCloudServiceAccountRequestCRDAndClient(crds);
  }

  public void initQuickStarts() {
    if (hasConsoleQuickStart()) {
      var quickstartCRD = client.apiextensions().v1beta1().customResourceDefinitions()
          .withName("consolequickstarts.console.openshift.io").get();
      var quickstartContext = CustomResourceDefinitionContext.fromCrd(quickstartCRD);
      var quickStartClient = client.customResource(quickstartContext);
      for (String quickstartFile : QUICKSTARTS) {
        try (InputStream fileStream =
            KafkaK8sClients.class.getClassLoader().getResourceAsStream(quickstartFile)) {
          var consoleQuickStart = quickStartClient.load(fileStream);
          var quickStartName =
              ((Map<String, Object>) consoleQuickStart.get("metadata")).get("name").toString();
          LOG.info(quickStartName);
          LOG.info(((List) quickStartClient
              .list(new ListOptionsBuilder().withKind("consolequickstarts")
                  .withFieldSelector(String.format("metadata.name=%s", quickStartName)).build())
              .get("items")).size());
          if (((List) quickStartClient
              .list(new ListOptionsBuilder().withKind("consolequickstarts")
                  .withFieldSelector(String.format("metadata.name=%s", quickStartName)).build())
              .get("items")).isEmpty()) {
            quickStartClient.create(consoleQuickStart);
          }
        } catch (Exception e) {
          LOG.error("Exception creating " + quickstartFile + ". Please create manually if missing.",
              e);
        }
      }
    }

  }

  private boolean hasConsoleQuickStart() {

    return client.apiextensions().v1beta1().customResourceDefinitions()
        .withName("consolequickstarts.console.openshift.io").get() != null;
  }

  public MixedOperation<KafkaConnection, KafkaConnectionList, Resource<KafkaConnection>> kafkaConnection() {
    KubernetesDeserializer.registerCustomKind(getApiVersion(KafkaConnection.class),
        akcCrd.getKind(), KafkaConnection.class);

    var akcCrdContext = CustomResourceDefinitionContext.fromCrd(this.akcCrd);

    // lets create a client for the CRD
    return client.customResources(akcCrdContext, KafkaConnection.class, KafkaConnectionList.class);
  }

  public MixedOperation<CloudServicesRequest, CloudServicesRequestList, Resource<CloudServicesRequest>> cloudServicesRequest() {
    KubernetesDeserializer.registerCustomKind(getApiVersion(CloudServicesRequest.class),
        cscrCrd.getKind(), CloudServicesRequest.class);

    var akcCrdContext = CustomResourceDefinitionContext.fromCrd(this.cscrCrd);

    // lets create a client for the CRD
    return client.customResources(akcCrdContext, CloudServicesRequest.class,
        CloudServicesRequestList.class);
  }

  public MixedOperation<CloudServiceAccountRequest, CloudServiceAccountRequestList, Resource<CloudServiceAccountRequest>> cloudServiceAccountRequest() {
    KubernetesDeserializer.registerCustomKind(getApiVersion(CloudServiceAccountRequest.class),
        csarCrd.getKind(), CloudServiceAccountRequest.class);

    var akcCrdContext = CustomResourceDefinitionContext.fromCrd(this.csarCrd);

    // lets create a client for the CRD
    return client.customResources(akcCrdContext, CloudServiceAccountRequest.class,
        CloudServiceAccountRequestList.class);
  }

  private CustomResourceDefinition initCloudServiceAccountRequestCRDAndClient(
      V1beta1ApiextensionAPIGroupDSL crds) {

    CustomResourceDefinition akcCrd;

    var crdsItems = crds.customResourceDefinitions().list().getItems();
    var kafkaConnectionCRDName = CustomResource.getCRDName(CloudServiceAccountRequest.class);

    var akcCrdOptional = crdsItems.stream()
        .filter(crd -> kafkaConnectionCRDName.equals(crd.getMetadata().getName())).findFirst();

    if (akcCrdOptional.isEmpty()) {
      LOG.info("Creating CloudServiceAccountRequest CRD");
      akcCrd = CustomResourceDefinitionContext
          .v1beta1CRDFromCustomResourceType(CloudServiceAccountRequest.class).build();
      client.apiextensions().v1beta1().customResourceDefinitions().create(akcCrd);
      LOG.info("CloudServiceAccountRequest CRD Created");
    } else {
      LOG.info("Found CloudServiceAccountRequest CRD");
      akcCrd = akcCrdOptional.get();
    }

    return akcCrd;
  }

  private CustomResourceDefinition initKafkaConnectionCRDAndClient(
      V1beta1ApiextensionAPIGroupDSL crds) {

    CustomResourceDefinition akcCrd;

    var crdsItems = crds.customResourceDefinitions().list().getItems();
    var kafkaConnectionCRDName = CustomResource.getCRDName(KafkaConnection.class);

    var akcCrdOptional = crdsItems.stream()
        .filter(crd -> kafkaConnectionCRDName.equals(crd.getMetadata().getName())).findFirst();

    if (akcCrdOptional.isEmpty()) {
      LOG.info("Creating KafkaConnection CRD");
      akcCrd = CustomResourceDefinitionContext
          .v1beta1CRDFromCustomResourceType(KafkaConnection.class).build();
      client.apiextensions().v1beta1().customResourceDefinitions().create(akcCrd);
      LOG.info("KafkaConnection CRD Created");
    } else {
      LOG.info("Found KafkaConnection CRD");
      akcCrd = akcCrdOptional.get();
    }

    return akcCrd;
  }

  private CustomResourceDefinition initCloudServicesRequestCRDAndClient(
      V1beta1ApiextensionAPIGroupDSL crds) {

    CustomResourceDefinition akcCrd;

    var crdsItems = crds.customResourceDefinitions().list().getItems();
    var cloudServicesRequestCRDName = CustomResource.getCRDName(CloudServicesRequest.class);

    var akcCrdOptional = crdsItems.stream()
        .filter(crd -> cloudServicesRequestCRDName.equals(crd.getMetadata().getName())).findFirst();

    if (akcCrdOptional.isEmpty()) {
      LOG.info("Creating CloudServicesRequest CRD");
      akcCrd = CustomResourceDefinitionContext
          .v1beta1CRDFromCustomResourceType(CloudServicesRequest.class).build();
      client.apiextensions().v1beta1().customResourceDefinitions().create(akcCrd);
      LOG.info("CloudServicesRequest CRD Created");
    } else {
      LOG.info("Found CloudServicesRequest CRD");
      akcCrd = akcCrdOptional.get();
    }

    return akcCrd;
  }

  /**
   * Computes the {@code apiVersion} associated with this HasMetadata implementation. The value is
   * derived from the {@link Group} and {@link Version} annotations.
   *
   * @param clazz the HasMetadata whose {@code apiVersion} we want to compute
   * @return the computed {@code apiVersion} or {@code null} if neither {@link Group} or
   *         {@link Version} annotations are present
   * @throws IllegalArgumentException if only one of {@link Group} or {@link Version} is provided
   */
  static String getApiVersion(Class<? extends HasMetadata> clazz) {
    final String group = getGroup(clazz);
    final String version = getVersion(clazz);
    if (group != null && version != null) {
      return group + "/" + version;
    }
    if (group != null || version != null) {
      throw new IllegalArgumentException("You need to specify both @" + Group.class.getSimpleName()
          + " and @" + Version.class.getSimpleName() + " annotations if you specify either");
    }
    return null;
  }

  /**
   * Retrieves the group associated with the specified HasMetadata as defined by the {@link Group}
   * annotation.
   *
   * @param clazz the HasMetadata whose group we want to retrieve
   * @return the associated group or {@code null} if the HasMetadata is not annotated with
   *         {@link Group}
   */
  static String getGroup(Class<? extends HasMetadata> clazz) {
    final Group group = clazz.getAnnotation(Group.class);
    return group != null ? group.value() : null;
  }

  /**
   * Retrieves the version associated with the specified HasMetadata as defined by the
   * {@link Version} annotation.
   *
   * @param clazz the HasMetadata whose version we want to retrieve
   * @return the associated version or {@code null} if the HasMetadata is not annotated with
   *         {@link Version}
   */
  static String getVersion(Class<? extends HasMetadata> clazz) {
    final Version version = clazz.getAnnotation(Version.class);
    return version != null ? version.value() : null;
  }
}
