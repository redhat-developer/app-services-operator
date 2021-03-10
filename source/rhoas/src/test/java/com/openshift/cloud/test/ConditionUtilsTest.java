package com.openshift.cloud.test;

import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.v1alpha.models.ManagedKafkaCondition;
import com.openshift.cloud.v1alpha.models.KafkaConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConditionUtilsTest {

  @Test
  public void finishedInitializedToUnknown() {
    var resource = new KafkaConnection();
    ConditionUtil.initializeConditions(resource);

    var resourceConditions = resource.getStatus().getConditions();

    Assertions.assertEquals(
        ManagedKafkaCondition.Status.Unknown,
        ConditionUtil.getCondition(resourceConditions, ManagedKafkaCondition.Type.Finished)
            .getStatus());
  }

  @Test
  public void settingExceptionSetsFinishedToFalse() {
    var resource = new KafkaConnection();
    ConditionUtil.initializeConditions(resource);

    var resourceConditions = resource.getStatus().getConditions();

    var exception =
        new ConditionAwareException(
            "Test Exception please Igrnore",
            new RuntimeException(),
            ManagedKafkaCondition.Type.AcccesTokenSecretValid,
            ManagedKafkaCondition.Status.False,
            "Ignore",
            "Ignore");
    ConditionUtil.setConditionFromException(resourceConditions, exception);

    Assertions.assertEquals(
        ManagedKafkaCondition.Status.False,
        ConditionUtil.getCondition(resourceConditions, ManagedKafkaCondition.Type.Finished)
            .getStatus());
  }
}
