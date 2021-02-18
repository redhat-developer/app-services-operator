package com.openshift.cloud.controllers;

import static com.openshift.cloud.v1alpha.models.ManagedKafkaCondition.Status.True;

import com.openshift.cloud.v1alpha.models.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ConditionUtil {
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
            .setType(ManagedKafkaCondition.Type.AcccesTokenSecretAvailable)
            .setReason("")
            .setMessage("")
            .setStatus(ManagedKafkaCondition.Status.Unknown),
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(ManagedKafkaCondition.Type.Finished)
            .setStatus(ManagedKafkaCondition.Status.Unknown),
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setReason("")
            .setMessage("")
            .setType(ManagedKafkaCondition.Type.UserKafkasUpToDate)
            .setStatus(ManagedKafkaCondition.Status.Unknown));
  }

  public static void setConditionFromException(
      List<ManagedKafkaCondition> conditions, ConditionAwareException e) {
    var condition = getCondition(conditions, e.getType());
    condition.setLastTransitionTime(isoNow());
    condition.setMessage(e.getConditionMessage());
    condition.setStatus(e.getStatus());
    condition.setReason(e.getReason());
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
      status.setConditions(managedServicesRequestDefaultConditions());
    }
  }

  private static List<ManagedKafkaCondition> managedKafkaServiceAccountRequestDefaultConditions() {
    return List.of(
        new ManagedKafkaCondition()
            .setLastTransitionTime(isoNow())
            .setType(ManagedKafkaCondition.Type.AcccesTokenSecretAvailable)
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
            .setStatus(ManagedKafkaCondition.Status.Unknown));
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
            .setType(ManagedKafkaCondition.Type.AcccesTokenSecretAvailable)
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
            .setStatus(ManagedKafkaCondition.Status.Unknown));
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
