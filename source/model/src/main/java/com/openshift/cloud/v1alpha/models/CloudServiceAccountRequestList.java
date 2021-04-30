package com.openshift.cloud.v1alpha.models;

import io.fabric8.kubernetes.client.CustomResourceList;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class CloudServiceAccountRequestList
    extends CustomResourceList<CloudServiceAccountRequest> {}
