package com.openshift.cloud.test.util;

import com.openshift.cloud.api.models.KafkaRequest;
import com.openshift.cloud.api.models.KafkaRequestList;
import com.openshift.cloud.api.models.ServiceAccount;
import com.openshift.cloud.beans.KafkaApiClient;
import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequest;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequestSpec;
import java.time.OffsetDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

@Alternative
@ApplicationScoped
public class MockKafkaApiClient extends KafkaApiClient {
  @Override
  public void createSecretForServiceAccount(
      CloudServiceAccountRequest resource, ServiceAccount serviceAccount)
      throws ConditionAwareException {
    throw new RuntimeException("STUB!!");
  }

  @Override
  public ServiceAccount createServiceAccount(
      CloudServiceAccountRequestSpec spec, String accessToken) throws ConditionAwareException {
    throw new RuntimeException("STUB!!");
  }

  @Override
  public KafkaRequest getKafkaById(String kafkaId, String accessToken)
      throws ConditionAwareException {
    throw new RuntimeException("STUB!!");
  }

  @Override
  public KafkaRequestList listKafkas(String accessToken) throws ConditionAwareException {
    var list = new KafkaRequestList();
    var request = new KafkaRequest();

    request.bootstrapServerHost("testHost");
    request.cloudProvider("cloudProvider");
    request.createdAt(OffsetDateTime.now());
    request.name("name");
    request.owner("owner");
    request.setId("1234567890");
    request.status("status");

    request.setUpdatedAt(OffsetDateTime.now());

    request.setRegion("region");

    list.addItemsItem(request);
    return list;
  }
}
