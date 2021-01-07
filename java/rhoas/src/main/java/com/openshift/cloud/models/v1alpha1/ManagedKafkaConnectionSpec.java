
package com.openshift.cloud.models.v1alpha1;

public class ManagedKafkaConnectionSpec {

    private String kafkaId;
    private Credentials credentials;


    /**
     * No args constructor for use in serialization
     * 
     */
    public ManagedKafkaConnectionSpec() {
    }

    /**
     * 
     * @param kafkaId
     * @param credentials
     */
    public ManagedKafkaConnectionSpec(String kafkaId, Credentials credentials) {
        super();
        this.kafkaId = kafkaId;
        this.credentials = credentials;
    }

    public String getKafkaId() {
        return kafkaId;
    }

    public void setKafkaId(String kafkaId) {
        this.kafkaId = kafkaId;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }


}
