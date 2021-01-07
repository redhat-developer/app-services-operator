
package com.openshift.cloud.v1alpha.models;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ManagedKafkaRequestSpec {

    private String accessTokenSecretName;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("accessTokenSecretName", accessTokenSecretName).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(accessTokenSecretName).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ManagedKafkaRequestSpec) == false) {
            return false;
        }
        ManagedKafkaRequestSpec rhs = ((ManagedKafkaRequestSpec) other);
        return new EqualsBuilder().append(accessTokenSecretName, rhs.accessTokenSecretName).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
