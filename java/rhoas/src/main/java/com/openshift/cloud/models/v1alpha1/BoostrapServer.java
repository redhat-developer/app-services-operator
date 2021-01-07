
package com.openshift.cloud.models.v1alpha1;

public class BoostrapServer {

    private String host;


    /**
     * No args constructor for use in serialization
     * 
     */
    public BoostrapServer() {
    }

    /**
     * 
     * @param host
     */
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


}
