
package com.openshift.cloud.v1alpha.models;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Credentials {

    private String serviceAccountSecretName;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Credentials() {
    }

    /**
     * 
     * @param serviceAccountSecretName
     */
    public Credentials(String serviceAccountSecretName) {
        super();
        this.serviceAccountSecretName = serviceAccountSecretName;
    }

    public String getServiceAccountSecretName() {
        return serviceAccountSecretName;
    }

    public void setServiceAccountSecretName(String serviceAccountSecretName) {
        this.serviceAccountSecretName = serviceAccountSecretName;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
