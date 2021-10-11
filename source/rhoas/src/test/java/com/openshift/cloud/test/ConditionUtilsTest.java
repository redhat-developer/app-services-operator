package com.openshift.cloud.test;

import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition;
import com.openshift.cloud.v1alpha.models.KafkaConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConditionUtilsTest {

  @Test
  public void finishedInitializedToUnknown() {
    var resource = new KafkaConnection();
    resource.getMetadata().setGeneration(1l);
    ConditionUtil.initializeConditions(resource);

    var resourceConditions = resource.getStatus().getConditions();

    Assertions.assertEquals(CloudServiceCondition.Status.Unknown,
        ConditionUtil.getCondition(resourceConditions, CloudServiceCondition.Type.Finished).getStatus());
  }

  @Test
  public void settingExceptionSetsFinishedToFalse() {
    var resource = new KafkaConnection();
    resource.getMetadata().setGeneration(1l);
    ConditionUtil.initializeConditions(resource);

    var resourceConditions = resource.getStatus().getConditions();

    var exception = new ConditionAwareException("Test Exception please Igrnore",
        new RuntimeException(), CloudServiceCondition.Type.AcccesTokenSecretValid,
        CloudServiceCondition.Status.False, "Ignore", "Ignore");
    ConditionUtil.setConditionFromException(resourceConditions, exception);

    Assertions.assertEquals(CloudServiceCondition.Status.False,
        ConditionUtil.getCondition(resourceConditions, CloudServiceCondition.Type.Finished).getStatus());
  }
}
