package com.openshift.cloud.utils;

import java.util.HashMap;
import java.util.Map;

/** Contains generic metadata for the resource connection instances */
public class ConnectionResourcesMetadata {

  private static final String UI_REF_TEMPLATE =
      "https://console.redhat.com/beta/application-services/streams/kafkas/%s";

  private static final String SRS_OAUTH_TEMPLATE = "%s/realms/%s/protocol/openid-connect/token";

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

  public static Map<String, String> buildServiceMetadata(String oauthHost, String oauthRealm) {
    var map = new HashMap<String, String>();
    map.put("provider", "rhoas");
    map.put("oauthTokenUrl", String.format(SRS_OAUTH_TEMPLATE, oauthHost, oauthRealm));
    map.put("type", "serviceregistry");
    return map;
  }
}
