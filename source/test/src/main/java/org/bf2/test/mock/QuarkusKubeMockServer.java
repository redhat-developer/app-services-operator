package org.bf2.test.mock;

import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.bf2.test.Environment;

/** Mock kubernetes CRUD server for quarkus test resource */
public class QuarkusKubeMockServer implements QuarkusTestResourceLifecycleManager {

  private KubernetesServer server;

  @Override
  public Map<String, String> start() {
    final Map<String, String> systemProps = new HashMap<>();
    systemProps.put(Config.KUBERNETES_TRUST_CERT_SYSTEM_PROPERTY, "true");
    systemProps.put(Config.KUBERNETES_AUTH_TRYKUBECONFIG_SYSTEM_PROPERTY, "false");
    systemProps.put(Config.KUBERNETES_AUTH_TRYSERVICEACCOUNT_SYSTEM_PROPERTY, "false");
    systemProps.put(Config.KUBERNETES_HTTP2_DISABLE, "true");

    server = createServer();
    server.before();
    try (NamespacedKubernetesClient client = server.getClient()) {
      systemProps.put(
          Config.KUBERNETES_MASTER_SYSTEM_PROPERTY, client.getConfiguration().getMasterUrl());
    }

    try {
      configureServer(server);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return systemProps;
  }

  protected KubernetesServer createServer() {
    return new KubernetesServer(useHttps(), true);
  }

  /**
   * Can be used by subclasses of {@code KubernetesServerTestResource} in order to setup the mock
   * server before the Quarkus application starts
   */
  public void configureServer(KubernetesServer mockServer) throws FileNotFoundException {
    // initialize with the crd
    for (Path crdPath : Environment.CRDS_PATH) {
      server
          .getClient()
          .load(new FileInputStream(crdPath.toString()))
          .get()
          .forEach(
              crd ->
                  server
                      .getClient()
                      .apiextensions()
                      .v1beta1()
                      .customResourceDefinitions()
                      .createOrReplace((CustomResourceDefinition) crd));
    }
  }

  @Override
  public void stop() {
    if (server != null) {
      server.after();
    }
  }

  /**
   * Find annotation @KubernetesMockServer and pass mock server into annotated property
   *
   * @param testInstance
   */
  public void inject(Object testInstance) {
    for (Class c = testInstance.getClass(); c != Object.class; c = c.getSuperclass()) {
      Field[] var3 = c.getDeclaredFields();
      int var4 = var3.length;

      for (int var5 = 0; var5 < var4; ++var5) {
        Field f = var3[var5];
        if (f.getAnnotation(QuarkusKubernetesMockServer.class) != null) {
          if (!KubernetesServer.class.isAssignableFrom(f.getType())) {
            throw new RuntimeException(
                "@KubernetesMockServer can only be used on fields of type KubernetesServer");
          }

          f.setAccessible(true);

          try {
            f.set(testInstance, this.server);
            return;
          } catch (Exception var8) {
            throw new RuntimeException(var8);
          }
        }
      }
    }
  }

  protected boolean useHttps() {
    return Boolean.getBoolean("quarkus.kubernetes-client.test.https");
  }
}
