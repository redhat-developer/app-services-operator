package com.openshift.cloud.v1alpha.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.sundr.builder.annotations.Buildable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Buildable(builderPackage = "io.fabric8.kubernetes.api.builder", editableEnabled = false)
public class ServiceRegistryConnectionSpec {

    private String serviceRegistryId;
    private String accessTokenSecretName;
    private Credentials credentials;


    
    public String getServiceRegistryId() {
        return serviceRegistryId;
    }
    public Credentials getCredentials() {
        return credentials;
    }
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
    public String getAccessTokenSecretName() {
        return accessTokenSecretName;
    }
    public void setAccessTokenSecretName(String accessTokenSecretName) {
        this.accessTokenSecretName = accessTokenSecretName;
    }
    public void setServiceRegistryId(String serviceRegistryId) {
        this.serviceRegistryId = serviceRegistryId;
    }

    

}
