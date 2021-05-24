package org.bf2.test.k8s;

import io.fabric8.kubernetes.api.model.APIService;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bf2.test.k8s.cmdClient.KubeCmdClient;
import org.bf2.test.k8s.cmdClient.Kubectl;
import org.bf2.test.k8s.cmdClient.Oc;

/** Abstraction over fabric8 client and cmd kube client */
public class KubeClient {
  private static final Logger LOGGER = LogManager.getLogger(KubeClient.class);

  @SuppressWarnings("rawtypes")
  private final KubeCmdClient cmdClient;

  private final KubernetesClient client;
  private static KubeClient instance;

  private KubeClient() {
    this.client = new DefaultKubernetesClient();
    if (isGenericKubernetes()) {
      LOGGER.info("Running tests against generic kubernetes cluster");
      this.cmdClient = new Kubectl();
    } else {
      LOGGER.info("Running tests against openshift cluster");
      this.cmdClient = new Oc();
    }
  }

  /**
   * Return singleton of kube client contains kubernetes client and cmd kube client
   *
   * @return
   */
  public static synchronized KubeClient getInstance() {
    if (instance == null) {
      instance = new KubeClient();
    }
    return instance;
  }

  public KubernetesClient client() {
    return this.client;
  }

  @SuppressWarnings("rawtypes")
  public KubeCmdClient cmdClient() {
    return this.cmdClient;
  }

  public boolean isGenericKubernetes() {
    List<APIService> services = new DefaultKubernetesClient().apiServices().list().getItems();
    for (APIService apiService : services) {
      if (apiService.getMetadata().getName().contains("openshift.io")) {
        return false;
      }
    }
    return true;
  }

  /**
   * Apply yaml files on kube cluster
   *
   * @param namespace namespace where to apply
   * @param streamManipulator replacer
   * @param paths folders
   */
  public void apply(String namespace, final Function<InputStream, InputStream> streamManipulator,
      final Path... paths) throws Exception {
    loadDirectories(streamManipulator, item -> item.inNamespace(namespace).createOrReplace(),
        paths);
  }

  /**
   * Apply resources from files
   *
   * @param namespace namessppace where to apply
   * @param paths folders
   */
  public void apply(String namespace, final Path... paths) throws Exception {
    apply(namespace, inputStream -> inputStream, paths);
  }

  /**
   * Delete resources from files
   *
   * @param streamManipulator replaces
   * @param paths folders
   */
  public void delete(final Function<InputStream, InputStream> streamManipulator,
      final Path... paths) throws Exception {
    loadDirectories(streamManipulator,
        o -> o.fromServer().get().forEach(item -> client.resource(item).delete()), paths);
  }

  private void loadDirectories(final Function<InputStream, InputStream> streamManipulator,
      Consumer<ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata>> consumer,
      final Path... paths) throws Exception {
    for (Path path : paths) {
      loadDirectory(streamManipulator, consumer, path);
    }
  }

  private void loadDirectory(final Function<InputStream, InputStream> streamManipulator,
      Consumer<ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata>> consumer,
      final Path path) throws Exception {

    LOGGER.info("Loading resources from: {}", path);

    Files.walkFileTree(path, new SimpleFileVisitor<>() {
      public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
          throws IOException {

        LOGGER.debug("Found: {}", file);

        if (!Files.isRegularFile(file)) {
          LOGGER.debug("File is not a regular file: {}", file);
          return FileVisitResult.CONTINUE;
        }

        if (!file.getFileName().toString().endsWith(".yaml")) {
          LOGGER.info("Skipping file: does not end with '.yaml': {}", file);
          return FileVisitResult.CONTINUE;
        }

        LOGGER.info("Processing: {}", file);

        try (InputStream f = Files.newInputStream(file)) {

          final InputStream in;
          if (streamManipulator != null) {
            in = streamManipulator.apply(f);
          } else {
            in = f;
          }

          if (in != null) {
            consumer.accept(client.load(in));
          }
        }

        return FileVisitResult.CONTINUE;
      }
    });
  }

  /** Namespace exists? */
  public boolean namespaceExists(String namespace) {
    return client.namespaces().withName(namespace).get() != null;
  }
}
