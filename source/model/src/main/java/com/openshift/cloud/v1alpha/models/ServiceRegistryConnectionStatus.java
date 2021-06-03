package com.openshift.cloud.v1alpha.models;

import io.sundr.builder.annotations.Buildable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Buildable(builderPackage = "io.fabric8.kubernetes.api.builder", editableEnabled = false)
public class ServiceRegistryConnectionStatus {

    private String message;
    private String updated;
    private String registryUrl;
    private String serviceAccountSecretName;
    private List<KafkaCondition> conditions;
    private Map<String, String> metadata;

    public String getMessage() {
        return message;
      }
    
      public void setMessage(String message) {
        this.message = message;
      }
    
      public String getUpdated() {
        return updated;
      }
    
      public void setUpdated(String updated) {
        this.updated = updated;
      }
    
      public String getServiceAccountSecretName() {
        return serviceAccountSecretName;
      }
    
      public void setServiceAccountSecretName(String serviceAccountSecretName) {
        this.serviceAccountSecretName = serviceAccountSecretName;
      }
    
      public List<KafkaCondition> getConditions() {
        return conditions;
      }
    
      public void setConditions(List<KafkaCondition> conditions) {
        this.conditions = conditions;
      }
    
      public String getRegistryUrl() {
        return registryUrl;
      }
    
      public void setRegistryUrl(String registryUrl) {
        this.registryUrl = registryUrl;
      }
    
      public Map<String, String> getMetadata() {
        return metadata;
      }
    
      public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
      }
}
