
package com.openshift.cloud.models.v1alpha1;

import java.util.List;

public class ManagedKafkaRequestStatus {

    private String lastUpdate;
    private List<UserKafka> userKafkas = null;


    /**
     * No args constructor for use in serialization
     * 
     */
    public ManagedKafkaRequestStatus() {
    }

    /**
     * 
     * @param userKafkas
     * @param lastUpdate
     */
    public ManagedKafkaRequestStatus(String lastUpdate, List<UserKafka> userKafkas) {
        super();
        this.lastUpdate = lastUpdate;
        this.userKafkas = userKafkas;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<UserKafka> getUserKafkas() {
        return userKafkas;
    }

    public void setUserKafkas(List<UserKafka> userKafkas) {
        this.userKafkas = userKafkas;
    }


}
