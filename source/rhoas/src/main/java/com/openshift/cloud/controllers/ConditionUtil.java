package com.openshift.cloud.controllers;

import static com.openshift.cloud.v1alpha.models.KafkaCondition.Status.False;
import static com.openshift.cloud.v1alpha.models.KafkaCondition.Status.True;

import com.openshift.cloud.v1alpha.models.*;
import com.openshift.cloud.v1alpha.models.KafkaCondition.Status;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class contains utility methods to set conditions on {@link CloudServicesRequest}, {@link
 * KafkaConnection}, and {@link CloudServiceAccountRequest}.
 *
 * <p>When you receive one of these objects in an operator, you should call initializeConditions
 * with that object. Operations on that object inside of a controller should be wrapped in a try
 * catch block.
 *
 * <p>The catch block should catch on a {@link ConditionAwareException} and call
 * setConditionFromException on that object.
 *
 * <p>If your controller does not encounter {@link ConditionAwareException}, please call
 * setAllConditionsTrue on the object before you write it back to k8s.
 */
public class ConditionUtil {

  private static final Logger LOG = Logger.getLogger(ConditionUtil.class.getName());

  public static void initializeConditions(CloudServicesRequest resource) {
    var status = resource.getStatus();
    if (status == null) {
      resource.setStatus(
          new CloudServicesRequestStatusBuilder()
              .withLastUpdate(isoNow())
              .withUserKafkas(new ArrayList<>())
              .withConditions(cloudServicesRequestDefaultConditions())
              .build());
    } else {
      status.setConditions(cloudServicesRequestDefaultConditions());
    }
  }

  private static List<KafkaCondition> cloudServicesRequestDefaultConditions() {
    return List.of(
        new KafkaCondition()
            .setLastTransitionTime(isoNow())
            .setType(KafkaCondition.Type.AcccesTokenSecretValid)
            .setReason("")
            .setMessage("")
            .setStatus(KafkaCondition.Status.Unknown),
        new KafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(KafkaCondition.Type.Finished)
            .setStatus(KafkaCondition.Status.Unknown),
        new KafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(KafkaCondition.Type.UserKafkasUpToDate)
            .setStatus(KafkaCondition.Status.Unknown));
  }

  /**
   * This method sets the condition on e to "false" as well as sets the Finished condition to false
   *
   * @param conditions a list of conditions that contains the condition in e as well as False.
   * @param e the exception
   */
  public static void setConditionFromException(
      List<KafkaCondition> conditions, ConditionAwareException e) {
    var condition = getCondition(conditions, e.getType());
    condition.setLastTransitionTime(isoNow());
    condition.setMessage(e.getConditionMessage());
    condition.setStatus(e.getStatus());
    condition.setReason(e.getReason());

    var finishedCondition = getCondition(conditions, KafkaCondition.Type.Finished);

    // There should be a finishedCondition, if not just write a debug log.
    if (finishedCondition == null) {
      LOG.log(Level.FINER, "No false condition was found while applying exception", e);
    } else {
      finishedCondition.setLastTransitionTime(isoNow());
      finishedCondition.setMessage(e.getConditionMessage());
      finishedCondition.setStatus(False);
      finishedCondition.setReason(e.getReason());
    }
  }

  public static KafkaCondition getCondition(
      List<KafkaCondition> resource, KafkaCondition.Type type) {
    return resource.stream()
        .filter(condition -> condition.getType() == (type))
        .findFirst()
        .orElseThrow(
            () -> {
              return new RuntimeException(
                  resource.stream().map(a -> a.getType().name()).collect(Collectors.joining(", "))
                      + " does not equal"
                      + type);
            });
  }

  private static String isoNow() {
    return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
  }

  public static void initializeConditions(CloudServiceAccountRequest resource) {
    var status = resource.getStatus();
    if (status == null) {
      resource.setStatus(
          new CloudServiceAccountRequestStatusBuilder()
              .withConditions(kafkaServiceAccountRequestDefaultConditions())
              .build());
    } else {
      status.setConditions(kafkaServiceAccountRequestDefaultConditions());
    }
  }

  private static List<KafkaCondition> kafkaServiceAccountRequestDefaultConditions() {
    return List.of(
        new KafkaCondition()
            .setLastTransitionTime(isoNow())
            .setType(KafkaCondition.Type.AcccesTokenSecretValid)
            .setReason("")
            .setMessage("")
            .setStatus(KafkaCondition.Status.Unknown),
        new KafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(KafkaCondition.Type.ServiceAccountCreated)
            .setStatus(KafkaCondition.Status.Unknown),
        new KafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(KafkaCondition.Type.ServiceAccountSecretCreated)
            .setStatus(KafkaCondition.Status.Unknown),
        new KafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(KafkaCondition.Type.Finished)
            .setStatus(Status.Unknown));
  }

  public static void setAllConditionsTrue(List<KafkaCondition> conditions) {
    conditions.forEach(
        condition -> {
          condition.setLastTransitionTime(isoNow());
          condition.setMessage("");
          condition.setStatus(True);
          condition.setReason("");
        });
  }

  public static void initializeConditions(KafkaConnection resource) {
    var status = resource.getStatus();
    if (status == null) {
      resource.setStatus(
          new KafkaConnectionStatusBuilder()
              .withConditions(kafkaConnectionDefaultConditions())
              .build());
    } else {
      status.setConditions(kafkaConnectionDefaultConditions());
    }
  }

  private static List<KafkaCondition> kafkaConnectionDefaultConditions() {
    return List.of(
        new KafkaCondition()
            .setLastTransitionTime(isoNow())
            .setType(KafkaCondition.Type.AcccesTokenSecretValid)
            .setReason("")
            .setMessage("")
            .setStatus(KafkaCondition.Status.Unknown),
        new KafkaCondition()
            .setLastTransitionTime(isoNow())
            .setType(KafkaCondition.Type.FoundKafkaById)
            .setReason("")
            .setMessage("")
            .setStatus(KafkaCondition.Status.Unknown),
        new KafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(KafkaCondition.Type.Finished)
            .setStatus(Status.Unknown));
  }

  public static boolean allTrue(List<KafkaCondition> conditions) {
    AtomicBoolean allTrue = new AtomicBoolean(true);
    for (var cond : conditions) {
      if (cond.getStatus() != True) {
        allTrue.set(false);
        break;
      }
    }
    ;
    return allTrue.get();
  }
}
