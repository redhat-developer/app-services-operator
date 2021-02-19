package com.openshift.cloud.controllers;

import static com.openshift.cloud.v1alpha.models.ManagedKafkaCondition.Status.False;
import static com.openshift.cloud.v1alpha.models.ManagedKafkaCondition.Status.True;

import com.openshift.cloud.v1alpha.models.*;
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
 * This class contains utility methods to set conditions on {@link ManagedServicesRequest}, {@link
 * ManagedKafkaConnection}, and {@link ManagedServiceAccountRequest}.
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

  public static void initializeConditions(ManagedServicesRequest resource) {
    var status = resource.getStatus();
    if (status == null) {
      resource.setStatus(
          new ManagedServicesRequestStatusBuilder()
              .withLastUpdate(isoNow())
              .withUserKafkas(new ArrayList<>())
              .withConditions(managedServicesRequestDefaultConditions())
              .build());
    } else {
      status.setConditions(managedServicesRequestDefaultConditions());
    }
  }

  private static List<ManagedKafkaCondition> managedServicesRequestDefaultConditions() {
    return List.of(
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setType(ManagedKafkaCondition.Type.AcccesTokenSecretValid)
            .setReason("")
            .setMessage("")
            .setStatus(ManagedKafkaCondition.Status.Unknown),
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(ManagedKafkaCondition.Type.Finished)
            .setStatus(ManagedKafkaCondition.Status.False),
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(ManagedKafkaCondition.Type.UserKafkasUpToDate)
            .setStatus(ManagedKafkaCondition.Status.Unknown));
  }

  /**
   * This method sets the condition on e to "false" as well as sets the Finished condition to false
   *
   * @param conditions a list of conditions that contains the condition in e as well as False.
   * @param e the exception
   */
  public static void setConditionFromException(
      List<ManagedKafkaCondition> conditions, ConditionAwareException e) {
    var condition = getCondition(conditions, e.getType());
    condition.setLastTransitionTime(isoNow());
    condition.setMessage(e.getConditionMessage());
    condition.setStatus(e.getStatus());
    condition.setReason(e.getReason());

    var finishedCondition = getCondition(conditions, ManagedKafkaCondition.Type.Finished);

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

  public static ManagedKafkaCondition getCondition(
      List<ManagedKafkaCondition> resource, ManagedKafkaCondition.Type type) {
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

  public static void initializeConditions(ManagedServiceAccountRequest resource) {
    var status = resource.getStatus();
    if (status == null) {
      resource.setStatus(
          new ManagedServiceAccountRequestStatusBuilder()
              .withConditions(managedKafkaServiceAccountRequestDefaultConditions())
              .build());
    } else {
      status.setConditions(managedKafkaServiceAccountRequestDefaultConditions());
    }
  }

  private static List<ManagedKafkaCondition> managedKafkaServiceAccountRequestDefaultConditions() {
    return List.of(
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setType(ManagedKafkaCondition.Type.AcccesTokenSecretValid)
            .setReason("")
            .setMessage("")
            .setStatus(ManagedKafkaCondition.Status.Unknown),
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(ManagedKafkaCondition.Type.ServiceAccountCreated)
            .setStatus(ManagedKafkaCondition.Status.Unknown),
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(ManagedKafkaCondition.Type.ServiceAccountSecretCreated)
            .setStatus(ManagedKafkaCondition.Status.Unknown),
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(ManagedKafkaCondition.Type.Finished)
            .setStatus(ManagedKafkaCondition.Status.False));
  }

  public static void setAllConditionsTrue(List<ManagedKafkaCondition> conditions) {
    conditions.forEach(
        condition -> {
          condition.setLastTransitionTime(isoNow());
          condition.setMessage("");
          condition.setStatus(True);
          condition.setReason("");
        });
  }

  public static void initializeConditions(ManagedKafkaConnection resource) {
    var status = resource.getStatus();
    if (status == null) {
      resource.setStatus(
          new ManagedKafkaConnectionStatusBuilder()
              .withConditions(managedKafkaConnectionDefaultConditions())
              .build());
    } else {
      status.setConditions(managedKafkaConnectionDefaultConditions());
    }
  }

  private static List<ManagedKafkaCondition> managedKafkaConnectionDefaultConditions() {
    return List.of(
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setType(ManagedKafkaCondition.Type.AcccesTokenSecretValid)
            .setReason("")
            .setMessage("")
            .setStatus(ManagedKafkaCondition.Status.Unknown),
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setType(ManagedKafkaCondition.Type.FoundKafkaById)
            .setReason("")
            .setMessage("")
            .setStatus(ManagedKafkaCondition.Status.Unknown),
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(ManagedKafkaCondition.Type.Finished)
            .setStatus(False));
  }

  public static boolean allTrue(List<ManagedKafkaCondition> conditions) {
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
