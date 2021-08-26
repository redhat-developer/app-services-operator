package com.openshift.cloud.utils;

import java.util.HashMap;
import java.util.Map;

/** Contains generic metadata for the resource connection instances */
public class ConnectionResourcesMetadata {

  private static final String UI_REF_TEMPLATE =
      "https://console.redhat.com/beta/application-services/streams/kafkas/%s";

  /**
   * Contains hardcoded values for all metadata for Kafka specific properties
   *
   * @param kafkaId
   * @return
   */
  public static Map<String, String> buildKafkaMetadata(String kafkaId) {
    var map = new HashMap<String, String>();
    map.put("saslMechanism", "PLAIN");
    map.put("securityProtocol", "SASL_SSL");
    map.put("cloudUI", String.format(UI_REF_TEMPLATE, kafkaId));
    map.put("provider", "rhoas");
    map.put("type", "kafka");
    return map;
  }

  public static Map<String, String> buildServiceMetadata(String oauthHost,String direction) {
    var map = new HashMap<String, String>();
    map.put("provider", "rhoas");
    map.put("oAuthHost", oauthHost);
    map.put("type", "serviceregistry");
    map.put("direction", direction);
    return map;
  }
}
