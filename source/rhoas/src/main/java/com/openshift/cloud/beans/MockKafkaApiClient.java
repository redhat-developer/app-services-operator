package com.openshift.cloud.beans;

import com.openshift.cloud.api.kas.models.KafkaRequest;
import com.openshift.cloud.api.kas.models.KafkaRequestList;
import com.openshift.cloud.api.kas.models.ServiceAccount;
import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequest;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequestSpec;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
@Alternative
public class MockKafkaApiClient extends KafkaApiClient {

  private static final KafkaRequest DEFAULT_KAFKA;

  private Map<String, KafkaRequest> kafkaRequestsById = new HashMap<>();

  static {
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
    DEFAULT_KAFKA = request;
  }

  public MockKafkaApiClient() {
    kafkaRequestsById.put(DEFAULT_KAFKA.getId(), DEFAULT_KAFKA);
  }

  @Override
  public ServiceAccount createServiceAccount(CloudServiceAccountRequestSpec spec,
      String accessToken) throws ConditionAwareException {
    ServiceAccount sa = new ServiceAccount();
    sa.setClientId("clientID");
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
    for (KafkaRequest request : kafkaRequestsById.values()) {
      list.addItemsItem(request);
    }
    return list;
  }

  /** This method adds a kafka to this mock that it returns. By default we return a DEFAULT_KAFKA */
  public void addKafka(KafkaRequest newKafka) {
    kafkaRequestsById.put(newKafka.getId(), newKafka);
  }

  /** Resets the kafka map to have only DEFAULT_KAFKA */
  public void resetKafkas() {
    kafkaRequestsById.clear();
    kafkaRequestsById.put(DEFAULT_KAFKA.getId(), DEFAULT_KAFKA);
  }
}
