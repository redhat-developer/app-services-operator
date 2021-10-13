package com.openshift.cloud.controllers;

import com.openshift.cloud.v1alpha.models.*;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition.Status;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition.Type;
import io.fabric8.kubernetes.client.CustomResource;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.DeleteControl;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/** This class manages conditions and checks for updates on startup. */
public abstract class AbstractCloudServicesController<T extends CustomResource>
    implements ResourceController<T> {

  public static final String COMPONENT_LABEL_KEY = "app.kubernetes.io/component";
  public static final String MANAGED_BY_LABEL_KEY = "app.kubernetes.io/managed-by";

  public static final String COMPONENT_LABEL_VALUE = "external-service";
  public static final String MANAGED_BY_LABEL_VALUE = "rhoas";

  public static List<Class<?>> CRS_THAT_REQUIRE_LABELS =
      Arrays.asList(KafkaConnection.class, ServiceRegistryConnection.class);

  private static final Logger LOG =
      Logger.getLogger(AbstractCloudServicesController.class.getName());


  /**
   * Implementations of this method should only change the status subresource. If you need to change
   * the resource, please implement a controller by extending ResourceController
   *
   * @param resource
   * @param context
   */
  abstract void doCreateOrUpdateResource(T resource, Context<T> context) throws Throwable;

  @Override
  /** This method is overriden by the proxies, but should not be overriden by you, the developer. */
  public UpdateControl<T> createOrUpdateResource(T resource, Context<T> context) {
    LOG.info("Updating resource " + resource.getCRDName());

    if (shouldProcess(resource)) {
      var updateLabels = requiresLabelUpdate(resource);
      sealedInitializeConditions(resource);
      try {

        doCreateOrUpdateResource(resource, context);
        sealedSetAllConditionsTrue(resource);
      } catch (ConditionAwareException e) {
        LOG.log(Level.SEVERE, e.getMessage(), e);
        sealedSetErrorConditions(resource, e);
      } catch (Throwable t) {
        LOG.log(Level.SEVERE, t.getMessage(), t);

        CloudServiceCondition finished = getSealedErrorCondition(resource, Type.Finished);
        finished.setReason(t.getClass().getName());
        finished.setMessage(t.getMessage());
        finished.setStatus(Status.False);
      }
      if (!updateLabels) {
        return UpdateControl.updateStatusSubResource(resource);
      } else {
        return UpdateControl.updateCustomResourceAndStatus(resource);
      }
    } else {
      return UpdateControl.noUpdate();
    }
  }

  private boolean requiresLabelUpdate(T resource) {
    var updateRequired = false;
    if (CRS_THAT_REQUIRE_LABELS.contains(resource.getClass())) {
      var labels = resource.getMetadata().getLabels();

      if (labels == null) {
        resource.getMetadata().setLabels(labels = new HashMap());
      }

      updateRequired = !(labels.containsKey(COMPONENT_LABEL_KEY));

      if (updateRequired) {
        labels.put(COMPONENT_LABEL_KEY, COMPONENT_LABEL_VALUE);
        labels.put(MANAGED_BY_LABEL_KEY, MANAGED_BY_LABEL_VALUE);
      }

    }
    return updateRequired;
  }

  /**
   * checks if resource should process
   *
   * @param resource
   * @return true if resource has no status or conditions or if condition.finished.generation <
   *         metadata.generation
   */
  private boolean shouldProcess(T resource) {

    if (resource.getStatus() == null) {
      return true;
    }

    List<CloudServiceCondition> conditions = getConditions(resource);

    if (conditions == null || conditions.isEmpty()) {
      return true;
    }

    var finishedCondition = ConditionUtil.getCondition(conditions, Type.Finished);
    if (finishedCondition.getLastTransitionGeneration() == null || finishedCondition
        .getLastTransitionGeneration() < resource.getMetadata().getGeneration()) {
      return true;
    }

    if (Status.True.equals(finishedCondition.getStatus())) {
      // everything is up to date and the resource had finished, then skip
      return false;
    } else {
      return true;
    }
  }

  private List<CloudServiceCondition> getConditions(T resource) {
    if (resource instanceof KafkaConnection) {
      return (((KafkaConnection) resource).getStatus().getConditions());
    } else if (resource instanceof ServiceRegistryConnection) {
      return (((ServiceRegistryConnection) resource).getStatus().getConditions());
    } else if (resource instanceof CloudServicesRequest) {
      return (((CloudServicesRequest) resource).getStatus().getConditions());
    } else if (resource instanceof CloudServiceAccountRequest) {
      return (((CloudServiceAccountRequest) resource).getStatus().getConditions());
    } else {
      throw new IllegalArgumentException(
          String.format("Resource of type %s is not supported", resource.getCRDName()));
    }
  }

  private void sealedSetAllConditionsTrue(T resource) {
    if (resource instanceof KafkaConnection) {
      ConditionUtil.setAllConditionsTrue(((KafkaConnection) resource).getStatus().getConditions());
    } else if (resource instanceof ServiceRegistryConnection) {
      ConditionUtil
          .setAllConditionsTrue(((ServiceRegistryConnection) resource).getStatus().getConditions());
    } else if (resource instanceof CloudServicesRequest) {
      ConditionUtil
          .setAllConditionsTrue(((CloudServicesRequest) resource).getStatus().getConditions());
    } else if (resource instanceof CloudServiceAccountRequest) {
      ConditionUtil.setAllConditionsTrue(
          ((CloudServiceAccountRequest) resource).getStatus().getConditions());
    }
  }

  private void sealedSetErrorConditions(T resource, ConditionAwareException e) {
    if (resource instanceof KafkaConnection) {
      ConditionUtil
          .setConditionFromException(((KafkaConnection) resource).getStatus().getConditions(), e);
    } else if (resource instanceof ServiceRegistryConnection) {
      ConditionUtil.setConditionFromException(
          ((ServiceRegistryConnection) resource).getStatus().getConditions(), e);
    } else if (resource instanceof CloudServicesRequest) {
      ConditionUtil.setConditionFromException(
          ((CloudServicesRequest) resource).getStatus().getConditions(), e);
    } else if (resource instanceof CloudServiceAccountRequest) {
      ConditionUtil.setConditionFromException(
          ((CloudServiceAccountRequest) resource).getStatus().getConditions(), e);
    } else {
      throw new IllegalArgumentException(
          String.format("Resource of type %s is not supported", resource.getCRDName()));
    }
  }

  /**
   * This method does typesafe calls to condition initialization per our CRs. It is called sealed
   * because it only checks those types, as if Java had sealed classes.
   *
   * @param resource
   */
  private void sealedInitializeConditions(T resource) {
    if (resource instanceof KafkaConnection) {
      ConditionUtil.initializeConditions((KafkaConnection) resource);
    } else if (resource instanceof ServiceRegistryConnection) {
      ConditionUtil.initializeConditions((ServiceRegistryConnection) resource);
    } else if (resource instanceof CloudServicesRequest) {
      ConditionUtil.initializeConditions((CloudServicesRequest) resource);
    } else if (resource instanceof CloudServiceAccountRequest) {
      ConditionUtil.initializeConditions((CloudServiceAccountRequest) resource);
    } else {
      throw new IllegalArgumentException(
          String.format("Resource of type %s is not supported", resource.getCRDName()));
    }
  }

  private CloudServiceCondition getSealedErrorCondition(T resource, Type type) {
    if (resource instanceof KafkaConnection) {
      return ConditionUtil.getCondition(((KafkaConnection) resource).getStatus().getConditions(),
          type);
    } else if (resource instanceof ServiceRegistryConnection) {
      return ConditionUtil
          .getCondition(((ServiceRegistryConnection) resource).getStatus().getConditions(), type);
    } else if (resource instanceof CloudServicesRequest) {
      return ConditionUtil
          .getCondition(((CloudServicesRequest) resource).getStatus().getConditions(), type);
    } else if (resource instanceof CloudServiceAccountRequest) {
      return ConditionUtil
          .getCondition(((CloudServiceAccountRequest) resource).getStatus().getConditions(), type);
    } else {
      throw new IllegalArgumentException(
          String.format("Resource of type %s is not supported", resource.getCRDName()));
    }
  }

  @Override
  public DeleteControl deleteResource(T resource, Context<T> context) {
    return DeleteControl.DEFAULT_DELETE;
  }
}
