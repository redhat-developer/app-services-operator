
package com.openshift.cloud.v1alpha.models;

import java.util.HashMap;
import java.util.Map;

public class UserKafka {

    private String id;
    private String created;
    private String provider;
    private String region;
    private String owner;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserKafka() {
    }

    /**
     * 
     * @param owner
     * @param provider
     * @param created
     * @param id
     * @param region
     */
    public UserKafka(String id, String created, String provider, String region, String owner) {
        super();
        this.id = id;
        this.created = created;
        this.provider = provider;
        this.region = region;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}
