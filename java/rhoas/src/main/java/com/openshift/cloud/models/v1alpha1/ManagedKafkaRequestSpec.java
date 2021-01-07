
package com.openshift.cloud.models.v1alpha1;

public class ManagedKafkaRequestSpec {

    private String accessTokenSecretName;


    /**
     * No args constructor for use in serialization
     * 
     */
    public ManagedKafkaRequestSpec() {
    }

    /**
     * 
     * @param accessTokenSecretName
     */
    public ManagedKafkaRequestSpec(String accessTokenSecretName) {
        super();
        this.accessTokenSecretName = accessTokenSecretName;
    }

    public String getAccessTokenSecretName() {
        return accessTokenSecretName;
    }

    public void setAccessTokenSecretName(String accessTokenSecretName) {
        this.accessTokenSecretName = accessTokenSecretName;
    }



}
