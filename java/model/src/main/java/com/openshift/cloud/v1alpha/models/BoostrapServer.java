package com.openshift.cloud.v1alpha.models;

import java.util.HashMap;
import java.util.Map;

public class BoostrapServer {

  private String host;
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  /** No args constructor for use in serialization */
  public BoostrapServer() {}

  /** @param host */
  public BoostrapServer(String host) {
    super();
    this.host = host;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }
}
