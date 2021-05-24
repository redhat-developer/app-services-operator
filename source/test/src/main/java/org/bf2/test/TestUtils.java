package org.bf2.test;

import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.utils.IOHelpers;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;

/** Test utils contains static help methods */
public class TestUtils {
  private static final Logger LOGGER = LogManager.getLogger(TestUtils.class);

  public static long waitFor(String description, long pollIntervalMs, long timeoutMs,
      BooleanSupplier ready) {
    return waitFor(description, pollIntervalMs, timeoutMs, ready, () -> {
    });
  }

  /**
   * Wait for specific lambda expression
   *
   * @param description description for logging
   * @param pollIntervalMs poll interval in ms
   * @param timeoutMs timeout in ms
   * @param ready lambda method for waiting
   * @param onTimeout lambda method which is called when timeout is reached
   * @return
   */
  public static long waitFor(String description, long pollIntervalMs, long timeoutMs,
      BooleanSupplier ready, Runnable onTimeout) {
    LOGGER.debug("Waiting for {}", description);
    long deadline = System.currentTimeMillis() + timeoutMs;
    while (true) {
      boolean result;
      try {
        result = ready.getAsBoolean();
      } catch (Exception e) {
        result = false;
      }
      long timeLeft = deadline - System.currentTimeMillis();
      if (result) {
        return timeLeft;
      }
      if (timeLeft <= 0) {
        onTimeout.run();
        WaitException waitException =
            new WaitException("Timeout after " + timeoutMs + " ms waiting for " + description);
        waitException.printStackTrace();
        throw waitException;
      }
      long sleepTime = Math.min(pollIntervalMs, timeLeft);
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace("{} not ready, will try again in {} ms ({}ms till timeout)", description,
            sleepTime, timeLeft);
      }
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        return deadline - System.currentTimeMillis();
      }
    }
  }

  public static File downloadYamlAndReplaceNamespace(String url, String namespace)
      throws IOException {
    File yamlFile = File.createTempFile("temp-file", ".yaml");

    try (InputStream bais = (InputStream) URI.create(url).toURL().openConnection().getContent();
        BufferedReader br = new BufferedReader(new InputStreamReader(bais, StandardCharsets.UTF_8));
        OutputStreamWriter osw =
            new OutputStreamWriter(new FileOutputStream(yamlFile), StandardCharsets.UTF_8)) {

      StringBuilder sb = new StringBuilder();

      String read;
      while ((read = br.readLine()) != null) {
        sb.append(read);
        sb.append("\n");
      }
      String yaml = sb.toString();
      yaml = yaml.replaceAll("namespace: .*", "namespace: " + namespace);
      yaml = yaml.replace(
          "securityContext:\n" + "        runAsNonRoot: true\n" + "        runAsUser: 65534", "");
      osw.write(yaml);
      return yamlFile;

    } catch (RuntimeException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static File replaceStringInYaml(String pathToOrigin, String originalns, String namespace)
      throws IOException {
    byte[] encoded;
    File yamlFile = File.createTempFile("temp-file", ".yaml");

    try (OutputStreamWriter osw =
        new OutputStreamWriter(new FileOutputStream(yamlFile), StandardCharsets.UTF_8)) {
      encoded = Files.readAllBytes(Paths.get(pathToOrigin));

      String yaml = new String(encoded, StandardCharsets.UTF_8);
      yaml = yaml.replaceAll(originalns, namespace);

      osw.write(yaml);
      return yamlFile.toPath().toFile();
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Path getLogPath(String folderName, ExtensionContext context) {
    String testMethod = context.getDisplayName();
    Class<?> testClass = context.getTestClass().get();
    return getLogPath(folderName, testClass, testMethod);
  }

  public static Path getLogPath(String folderName, TestInfo info) {
    String testMethod = info.getDisplayName();
    Class<?> testClass = info.getTestClass().get();
    return getLogPath(folderName, testClass, testMethod);
  }

  public static Path getLogPath(String folderName, Class<?> testClass, String testMethod) {
    Path path = Environment.LOG_DIR.resolve(Paths.get(folderName, testClass.getName()));
    if (testMethod != null) {
      path = path.resolve(testMethod.replace("(", "").replace(")", ""));
    }
    return path;
  }

  public static void logWithSeparator(String pattern, String text) {
    LOGGER.info("=======================================================================");
    LOGGER.info(pattern, text);
    LOGGER.info("=======================================================================");
  }

  /**
   * Check all containers state in pod and return boolean status if po is running
   *
   * @param p pod
   */
  public static boolean isPodReady(Pod p) {
    return p.getStatus().getContainerStatuses().stream().allMatch(ContainerStatus::getReady);
  }

  /**
   * Replacer function replacing values in input stream and returns modified input stream
   *
   * @param values map of values for replace
   */
  public static Function<InputStream, InputStream> replacer(final Map<String, String> values) {
    return in -> {
      try {
        String strContent = IOHelpers.readFully(in);
        for (Map.Entry<String, String> replacer : values.entrySet()) {
          strContent = strContent.replace(replacer.getKey(), replacer.getValue());
        }
        return new ByteArrayInputStream(strContent.getBytes());
      } catch (IOException ex) {
        throw KubernetesClientException.launderThrowable(ex);
      }
    };
  }
}
