package com.openshift.cloud.beans;

import com.openshift.cloud.api.models.KafkaRequest;
import com.openshift.cloud.api.models.KafkaRequestList;
import com.openshift.cloud.api.models.ServiceAccount;
import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequest;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequestSpec;
import java.time.OffsetDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

@ApplicationScoped
@Alternative
public class MockKafkaApiClient extends KafkaApiClient {
  @Override
  public void createSecretForServiceAccount(
      CloudServiceAccountRequest resource, ServiceAccount serviceAccount)
      throws ConditionAwareException {
    // NOOP
    // The secret is pseudo hard coded and will be returned as a hard coded value by the k8s mock
  }

  @Override
  public ServiceAccount createServiceAccount(
      CloudServiceAccountRequestSpec spec, String accessToken) throws ConditionAwareException {
    ServiceAccount sa = new ServiceAccount();
    sa.setClientID("clientID");
    sa.setClientSecret("clientSecret");
    sa.setDescription(spec.getServiceAccountDescription());
    sa.setId("123456789");
    sa.setName(spec.getServiceAccountName());
    return sa;
  }

  @Override
  public KafkaRequest getKafkaById(String kafkaId, String accessToken)
      throws ConditionAwareException {
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
    return request;
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
