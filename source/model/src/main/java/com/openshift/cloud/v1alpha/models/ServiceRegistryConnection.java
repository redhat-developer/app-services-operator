package com.openshift.cloud.v1alpha.models;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Plural;
import io.fabric8.kubernetes.model.annotation.Version;
import io.sundr.builder.annotations.Buildable;
import io.sundr.builder.annotations.BuildableReference;

@Plural("serviceregistryconnection")
@Group("rhoas.redhat.com")
@Version("v1alpha1")
@Buildable(builderPackage = "io.fabric8.kubernetes.api.builder", editableEnabled = false,
    refs = @BuildableReference(CustomResource.class))
public class ServiceRegistryConnection  extends CustomResource<ServiceRegistryConnectionSpec, ServiceRegistryConnectionStatus>
implements Namespaced {


    @Override
    public ServiceRegistryConnectionSpec getSpec() {
      return super.getSpec();
    }
  
    @Override
    public void setSpec(ServiceRegistryConnectionSpec spec) {
      super.setSpec(spec);
    }
  
    @Override
    public ServiceRegistryConnectionStatus getStatus() {
      return super.getStatus();
    }
  
    @Override
    public void setStatus(ServiceRegistryConnectionStatus status) {
      super.setStatus(status);
    }
  
    @Override
    public ObjectMeta getMetadata() {
      return super.getMetadata();
    }
  
    @Override
    public void setMetadata(ObjectMeta metadata) {
      super.setMetadata(metadata);
    }

}
