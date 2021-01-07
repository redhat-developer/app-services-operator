
package com.openshift.cloud.v1alpha.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ManagedKafkaRequestStatus {

    private String lastUpdate;
    private List<UserKafka> userKafkas = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}
