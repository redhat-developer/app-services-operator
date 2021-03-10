package com.openshift.cloud.beans;

import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequest;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequestList;
import com.openshift.cloud.v1alpha.models.CloudServicesRequest;
import com.openshift.cloud.v1alpha.models.CloudServicesRequestList;
import com.openshift.cloud.v1alpha.models.KafkaConnection;
import com.openshift.cloud.v1alpha.models.KafkaConnectionList;
import io.fabric8.kubernetes.api.model.HasMetadata;
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
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jboss.logging.Logger;

/**
 * This class is processed at startup and checks that CRDs are installed and provides apiClients for
 * use
 */
@Singleton
public final class KafkaK8sClients {

  private static final Logger LOG = Logger.getLogger(KafkaK8sClients.class.getName());

  @Inject KubernetesClient client;

  private CustomResourceDefinition mkcCrd;
  private CustomResourceDefinition mscrCrd;
  private CustomResourceDefinition msarCrd;

  @PostConstruct
  public void init() {
    LOG.info("Init");

    var crds = client.apiextensions().v1beta1();

    this.mkcCrd = initKafkaConnectionCRDAndClient(crds);
    this.mscrCrd = initCloudServicesRequestCRDAndClient(crds);
    this.msarCrd = initCloudServiceAccountRequestCRDAndClient(crds);
  }

  public MixedOperation<KafkaConnection, KafkaConnectionList, Resource<KafkaConnection>>
      kafkaConnection() {
    KubernetesDeserializer.registerCustomKind(
        getApiVersion(KafkaConnection.class), mkcCrd.getKind(), KafkaConnection.class);

    var mkcCrdContext = CustomResourceDefinitionContext.fromCrd(this.mkcCrd);

    // lets create a client for the CRD
    return client.customResources(mkcCrdContext, KafkaConnection.class, KafkaConnectionList.class);
  }

  public MixedOperation<
          CloudServicesRequest, CloudServicesRequestList, Resource<CloudServicesRequest>>
      cloudServicesRequest() {
    KubernetesDeserializer.registerCustomKind(
        getApiVersion(CloudServicesRequest.class), mscrCrd.getKind(), CloudServicesRequest.class);

    var mkcCrdContext = CustomResourceDefinitionContext.fromCrd(this.mscrCrd);

    // lets create a client for the CRD
    return client.customResources(
        mkcCrdContext, CloudServicesRequest.class, CloudServicesRequestList.class);
  }

  public MixedOperation<
          CloudServiceAccountRequest,
          CloudServiceAccountRequestList,
          Resource<CloudServiceAccountRequest>>
      cloudServiceAccountRequest() {
    KubernetesDeserializer.registerCustomKind(
        getApiVersion(CloudServiceAccountRequest.class),
        msarCrd.getKind(),
        CloudServiceAccountRequest.class);

    var mkcCrdContext = CustomResourceDefinitionContext.fromCrd(this.msarCrd);

    // lets create a client for the CRD
    return client.customResources(
        mkcCrdContext, CloudServiceAccountRequest.class, CloudServiceAccountRequestList.class);
  }

  private CustomResourceDefinition initCloudServiceAccountRequestCRDAndClient(
      V1beta1ApiextensionAPIGroupDSL crds) {

    CustomResourceDefinition mkcCrd;

    var crdsItems = crds.customResourceDefinitions().list().getItems();
    var kafkaConnectionCRDName = CustomResource.getCRDName(CloudServiceAccountRequest.class);

    var mkcCrdOptional =
        crdsItems.stream()
            .filter(crd -> kafkaConnectionCRDName.equals(crd.getMetadata().getName()))
            .findFirst();

    if (mkcCrdOptional.isEmpty()) {
      LOG.info("Creating CloudServiceAccountRequest CRD");
      mkcCrd =
          CustomResourceDefinitionContext.v1beta1CRDFromCustomResourceType(
                  CloudServiceAccountRequest.class)
              .build();
      client.apiextensions().v1beta1().customResourceDefinitions().create(mkcCrd);
      LOG.info("CloudServiceAccountRequest CRD Created");
    } else {
      LOG.info("Found CloudServiceAccountRequest CRD");
      mkcCrd = mkcCrdOptional.get();
    }

    return mkcCrd;
  }

  private CustomResourceDefinition initKafkaConnectionCRDAndClient(
      V1beta1ApiextensionAPIGroupDSL crds) {

    CustomResourceDefinition mkcCrd;

    var crdsItems = crds.customResourceDefinitions().list().getItems();
    var kafkaConnectionCRDName = CustomResource.getCRDName(KafkaConnection.class);

    var mkcCrdOptional =
        crdsItems.stream()
            .filter(crd -> kafkaConnectionCRDName.equals(crd.getMetadata().getName()))
            .findFirst();

    if (mkcCrdOptional.isEmpty()) {
      LOG.info("Creating KafkaConnection CRD");
      mkcCrd =
          CustomResourceDefinitionContext.v1beta1CRDFromCustomResourceType(KafkaConnection.class)
              .build();
      client.apiextensions().v1beta1().customResourceDefinitions().create(mkcCrd);
      LOG.info("KafkaConnection CRD Created");
    } else {
      LOG.info("Found KafkaConnection CRD");
      mkcCrd = mkcCrdOptional.get();
    }

    return mkcCrd;
  }

  private CustomResourceDefinition initCloudServicesRequestCRDAndClient(
      V1beta1ApiextensionAPIGroupDSL crds) {

    CustomResourceDefinition mkcCrd;

    var crdsItems = crds.customResourceDefinitions().list().getItems();
    var cloudServicesRequestCRDName = CustomResource.getCRDName(CloudServicesRequest.class);

    var mkcCrdOptional =
        crdsItems.stream()
            .filter(crd -> cloudServicesRequestCRDName.equals(crd.getMetadata().getName()))
            .findFirst();

    if (mkcCrdOptional.isEmpty()) {
      LOG.info("Creating CloudServicesRequest CRD");
      mkcCrd =
          CustomResourceDefinitionContext.v1beta1CRDFromCustomResourceType(
                  CloudServicesRequest.class)
              .build();
      client.apiextensions().v1beta1().customResourceDefinitions().create(mkcCrd);
      LOG.info("CloudServicesRequest CRD Created");
    } else {
      LOG.info("Found CloudServicesRequest CRD");
      mkcCrd = mkcCrdOptional.get();
    }

    return mkcCrd;
  }

  /**
   * Computes the {@code apiVersion} associated with this HasMetadata implementation. The value is
   * derived from the {@link Group} and {@link Version} annotations.
   *
   * @param clazz the HasMetadata whose {@code apiVersion} we want to compute
   * @return the computed {@code apiVersion} or {@code null} if neither {@link Group} or {@link
   *     Version} annotations are present
   * @throws IllegalArgumentException if only one of {@link Group} or {@link Version} is provided
   */
  static String getApiVersion(Class<? extends HasMetadata> clazz) {
    final String group = getGroup(clazz);
    final String version = getVersion(clazz);
    if (group != null && version != null) {
      return group + "/" + version;
    }
    if (group != null || version != null) {
      throw new IllegalArgumentException(
          "You need to specify both @"
              + Group.class.getSimpleName()
              + " and @"
              + Version.class.getSimpleName()
              + " annotations if you specify either");
    }
    return null;
  }

  /**
   * Retrieves the group associated with the specified HasMetadata as defined by the {@link Group}
   * annotation.
   *
   * @param clazz the HasMetadata whose group we want to retrieve
   * @return the associated group or {@code null} if the HasMetadata is not annotated with {@link
   *     Group}
   */
  static String getGroup(Class<? extends HasMetadata> clazz) {
    final Group group = clazz.getAnnotation(Group.class);
    return group != null ? group.value() : null;
  }

  /**
   * Retrieves the version associated with the specified HasMetadata as defined by the {@link
   * Version} annotation.
   *
   * @param clazz the HasMetadata whose version we want to retrieve
   * @return the associated version or {@code null} if the HasMetadata is not annotated with {@link
   *     Version}
   */
  static String getVersion(Class<? extends HasMetadata> clazz) {
    final Version version = clazz.getAnnotation(Version.class);
    return version != null ? version.value() : null;
  }
}
