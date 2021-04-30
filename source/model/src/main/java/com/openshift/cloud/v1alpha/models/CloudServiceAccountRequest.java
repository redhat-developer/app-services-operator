package com.openshift.cloud.v1alpha.models;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Plural;
import io.fabric8.kubernetes.model.annotation.Version;
import io.sundr.builder.annotations.Buildable;
import io.sundr.builder.annotations.BuildableReference;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Plural("cloudserviceaccountrequests")
@Group("rhoas.redhat.com")
@Version("v1alpha1")
@Buildable(
    builderPackage = "io.fabric8.kubernetes.api.builder",
    editableEnabled = false,
    refs = @BuildableReference(CustomResource.class))
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class CloudServiceAccountRequest
    extends CustomResource<CloudServiceAccountRequestSpec, CloudServiceAccountRequestStatus>
    implements Namespaced {

}
