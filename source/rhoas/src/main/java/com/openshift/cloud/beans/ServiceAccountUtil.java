package com.openshift.cloud.beans;

import com.openshift.cloud.api.kas.invoker.ApiException;
import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.controllers.ConditionUtil;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

/** Utility bean to exchange offline tokens for access tokens */
@ApplicationScoped
public class SecretUtil {
  private static final Logger LOG = Logger.getLogger(SecretUtil.class.getName());

  private static final String TOKEN_SECRET_KEY = "clientID";

  @Inject
  KubernetesClient k8sClient;

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
  public String getServiceAccountSecret(String secretName, String namespace)
      throws ConditionAwareException {
    try {
      var token = k8sClient.secrets().inNamespace(namespace).withName(secretName).get();
      if (token != null) {
        var offlineToken = token.getData().get(TOKEN_SECRET_KEY);
        // decode may throw IllegalArgumentException
        offlineToken = new String(Base64.getDecoder().decode(offlineToken));
        return offlineToken;
      }
      // We expect the token to exist, and if it doesn't raise an exception.
      throw new ConditionAwareException(String.format("Missing Offline Token Secret %s", secretName),
              null, CloudServiceCondition.Type.AcccesTokenSecretValid, CloudServiceCondition.Status.False,
              "ConditionAwareException", String.format("Missing Offline Token Secret %s", secretName));
    } catch (ConditionAwareException ex) {
      // Log and rethrow exception
      LOG.log(Level.SEVERE, ex.getMessage());
      throw ex;
    } catch (Throwable ex) {
      // Unexpected exception or error (NPE, IOException, out of memory, etc)
      LOG.log(Level.SEVERE, ex.getMessage());
      throw new ConditionAwareException(ex.getMessage(), ex,
              CloudServiceCondition.Type., CloudServiceCondition.Status.False,
              ex.getClass().getName(), ex.getMessage());
    }
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
                  "client_id", clientId, "refresh_token", offlineToken))
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
        var apiError = ConditionUtil.getStandarizedErrorMessage(response.statusCode(),
            new ApiException(response.statusCode(), null));
        throw new ConditionAwareException(response.body(), null,
            CloudServiceCondition.Type.AcccesTokenSecretValid, CloudServiceCondition.Status.False,
            String.format("Http Error Code %d", response.statusCode()), apiError);
      }
    } catch (IOException | InterruptedException e) {
      throw new ConditionAwareException(e.getMessage(), e,
          CloudServiceCondition.Type.AcccesTokenSecretValid, CloudServiceCondition.Status.False,
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

  public String getTokenPath() {
    return this.tokenPath;
  }

}
