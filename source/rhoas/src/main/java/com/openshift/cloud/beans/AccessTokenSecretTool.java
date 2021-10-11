package com.openshift.cloud.beans;

import com.openshift.cloud.api.kas.invoker.ApiException;
import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.v1alpha.models.KafkaCondition;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/** Utility bean to exchange offline tokens for access tokens */
@ApplicationScoped
public class AccessTokenSecretTool {
  private static final Logger LOG = Logger.getLogger(AccessTokenSecretTool.class.getName());

  private static final String ACCESS_TOKEN_SECRET_KEY = "value";

  @ConfigProperty(name = "auth.serverUrl")
  String authServerUrl;

  @ConfigProperty(name = "auth.clientId", defaultValue = "cloud-services")
  String clientId;

  @ConfigProperty(name = "auth.tokenPath", defaultValue = "protocol/openid-connect/token")
  String tokenPath;

  @Inject
  KubernetesClient k8sClient;

  /**
   * This method exchanges secret values for an access token.
   *
   * @param accessTokenSecretName the secret which contains an offline token on the "value" key
   * @param namespace namespace of secret
   * @return a valid access token
   */
  public String getAccessToken(String accessTokenSecretName, String namespace)
      throws ConditionAwareException {
    try {
      if (isNullOrEmpty(accessTokenSecretName, namespace)) {
        throw new IllegalArgumentException(
            "accessTokenSecretName and namespace must be provided and exist.");
      }
      var offlineToken = getOfflineTokenFromSecret(accessTokenSecretName, namespace);
      var accessToken = exchangeToken(offlineToken);
      return accessToken;
    } catch (ConditionAwareException ex) {
      // Log and rethrow exception
      LOG.log(Level.SEVERE, ex.getMessage());
      throw ex;
    } catch (Throwable ex) {
      // Unexpected exception or error (NPE, IOException, out of memory, etc)
      LOG.log(Level.SEVERE, ex.getMessage());
      throw new ConditionAwareException(ex.getMessage(), ex,
          KafkaCondition.Type.AcccesTokenSecretValid, KafkaCondition.Status.False,
          ex.getClass().getName(), ex.getMessage());
    }
  }

  private boolean isNullOrEmpty(String... strings) {
    for (String string : strings) {
      if (string == null || string.isEmpty()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Given a secret and a namespace load the secret and decode the value with the key "value"
   *
   * @param secretName name of the secret
   * @param namespace namespace of the secret
   * @return the 64 decoded value of namespace/secretName
   * @throws ConditionAwareException if the secret does not exist
   * @throws IllegalArgumentException if secret value is not in valid Base64
   */
  private String getOfflineTokenFromSecret(String secretName, String namespace)
      throws ConditionAwareException {
    var token = k8sClient.secrets().inNamespace(namespace).withName(secretName).get();
    if (token != null) {
      var offlineToken = token.getData().get(ACCESS_TOKEN_SECRET_KEY);
      // decode may throw IllegalArgumentException
      offlineToken = new String(Base64.getDecoder().decode(offlineToken));
      return offlineToken;
    }
    // We expect the token to exist, and if it doesn't raise an exception.
    throw new ConditionAwareException(String.format("Missing Offline Token Secret %s", secretName),
        null, KafkaCondition.Type.AcccesTokenSecretValid, KafkaCondition.Status.False,
        "ConditionAwareException", String.format("Missing Offline Token Secret %s", secretName));
  }

  /**
   * This method exchanges an offline token for a new refresh token
   *
   * @param offlineToken the token from ss.redhat.com
   * @return a token to be used as a bearer token to authorize the user
   * @throws ConditionAwareException
   */
  private String exchangeToken(String offlineToken) throws ConditionAwareException {
    try {
      HttpRequest request =
          HttpRequest.newBuilder().uri(URI.create(authServerUrl + "/" + tokenPath))
              .header("content-type", "application/x-www-form-urlencoded")
              .timeout(Duration.ofMinutes(2)).POST(ofFormData("grant_type", "refresh_token",
                  "client_id", "cloud-services", "refresh_token", offlineToken))
              .build();

      HttpClient client = HttpClient.newBuilder().build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        var tokens = response.body();
        var json = new JsonObject(tokens);
        return json.getString("access_token");
      } else {
        LOG.log(Level.SEVERE,
            String.format("Exchange token failed with error %s", response.body()));
        // Reusing API error but only with status code
        var apiError =
            ConditionUtil.getStandarizedErrorMessage(response.statusCode(), new ApiException(response.statusCode(), null));
        throw new ConditionAwareException(response.body(), null,
            KafkaCondition.Type.AcccesTokenSecretValid, KafkaCondition.Status.False,
            String.format("Http Error Code %d", response.statusCode()), apiError);
      }
    } catch (IOException | InterruptedException e) {
      throw new ConditionAwareException(e.getMessage(), e,
          KafkaCondition.Type.AcccesTokenSecretValid, KafkaCondition.Status.False,
          e.getClass().getName(), e.getMessage());
    }
  }

  private static HttpRequest.BodyPublisher ofFormData(String... data) {
    var builder = new StringBuilder();
    if (data.length % 2 == 1) {
      throw new IllegalArgumentException(
          "Data must be key value pairs, but an number of data were given. ");
    }

    for (int index = 0; index < data.length; index += 2) {
      if (builder.length() > 0) {
        builder.append("&");
      }
      builder.append(URLEncoder.encode(data[index], StandardCharsets.UTF_8));
      builder.append("=");
      builder.append(URLEncoder.encode(data[index + 1], StandardCharsets.UTF_8));
    }

    return HttpRequest.BodyPublishers.ofString(builder.toString());
  }
}
