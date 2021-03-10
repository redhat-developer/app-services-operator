package com.openshift.cloud.test;

import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.v1alpha.models.KafkaCondition;
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
        KafkaCondition.Status.Unknown,
        ConditionUtil.getCondition(resourceConditions, KafkaCondition.Type.Finished)
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
            KafkaCondition.Type.AcccesTokenSecretValid,
            KafkaCondition.Status.False,
            "Ignore",
            "Ignore");
    ConditionUtil.setConditionFromException(resourceConditions, exception);

    Assertions.assertEquals(
        KafkaCondition.Status.False,
        ConditionUtil.getCondition(resourceConditions, KafkaCondition.Type.Finished)
            .getStatus());
  }
}
