package org.bf2.test.mock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotation for KubernetesServer property in test class where extension pass mock kube server */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuarkusKubernetesMockServer {}
