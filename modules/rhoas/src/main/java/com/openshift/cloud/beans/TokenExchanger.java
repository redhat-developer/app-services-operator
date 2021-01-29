package com.openshift.cloud.beans;

import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import javax.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/** Utility bean to exchange offline tokens for access tokens */
@Singleton
public class TokenExchanger {

  @ConfigProperty(
      name = "auth.serverUrl",
      defaultValue = "https://sso.redhat.com/auth/realms/redhat-external")
  String authServerUrl;

  @ConfigProperty(name = "auth.clientId", defaultValue = "cloud-services")
  String clientId;

  @ConfigProperty(name = "auth.tokenPath", defaultValue = "protocol/openid-connect/token")
  String tokenPath;

  /**
   * This method exchanges an offline token for a new refresh token
   *
   * @param offlineToken the token from ss.redhat.com
   * @return a token to be used as a bearer token to authorize the user
   */
  public String getToken(String offlineToken) {
    try {
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create(authServerUrl + "/" + tokenPath))
              .header("content-type", "application/x-www-form-urlencoded")
              .timeout(Duration.ofMinutes(2))
              .POST(
                  ofFormData(
                      "grant_type",
                      "refresh_token",
                      "client_id",
                      "cloud-services",
                      "refresh_token",
                      offlineToken))
              .build();

      HttpClient client = HttpClient.newBuilder().build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() == 200) {
        var tokens = response.body();
        var json = new JsonObject(tokens);
        return json.getString("access_token");
      } else {
        throw new RuntimeException(response.body());
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static HttpRequest.BodyPublisher ofFormData(String... data) {

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
