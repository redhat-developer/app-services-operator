package com.openshift.cloud.beans;

import com.openshift.cloud.api.kas.models.ServiceAccount;
import com.openshift.cloud.controllers.ConditionAwareException;
import com.openshift.cloud.v1alpha.models.CloudServiceAccountRequest;
import com.openshift.cloud.v1alpha.models.CloudServiceCondition;
import io.fabric8.kubernetes.api.model.OwnerReferenceBuilder;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Utility bean to manage service accounts */
@ApplicationScoped
public class ServiceAccountUtil {
  private static final Logger LOG = Logger.getLogger(ServiceAccountUtil.class.getName());

  private static final String TOKEN_CLIENT_ID_KEY = "client-id";
  private static final String TOKEN_CLIENT_SECRET_KEY = "client-secret";
  @Inject
  KubernetesClient k8sClient;

  public void createSecretForServiceAccount(CloudServiceAccountRequest resource,
                                            ServiceAccount serviceAccount) throws ConditionAwareException {
    try {
      var secret = new SecretBuilder().editOrNewMetadata()
              .withName(resource.getSpec().getServiceAccountSecretName())
              .withNamespace(resource.getMetadata().getNamespace())
              .withOwnerReferences(List.of(new OwnerReferenceBuilder()
                      .withApiVersion(resource.getApiVersion()).withController(true)
                      .withKind(resource.getKind()).withName(resource.getMetadata().getName())
                      .withUid(resource.getMetadata().getUid()).build()))
              .endMetadata()
              .withData(Map.of(TOKEN_CLIENT_SECRET_KEY, Base64.getEncoder()
                              .encodeToString(serviceAccount.getClientSecret().getBytes(Charset.defaultCharset())),
                      TOKEN_CLIENT_ID_KEY,
                      Base64.getEncoder()
                              .encodeToString(serviceAccount.getClientId().getBytes(Charset.defaultCharset()))))
              .build();

      k8sClient.secrets().inNamespace(secret.getMetadata().getNamespace()).create(secret);
    } catch (Exception e) {
      throw new ConditionAwareException(e.getMessage(), e,
              CloudServiceCondition.Type.ServiceAccountSecretCreated,
              CloudServiceCondition.Status.False, e.getClass().getName(), e.getMessage());
    }
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
      var serviceAccount = k8sClient.secrets().inNamespace(namespace).withName(secretName).get();
      if (serviceAccount != null) {
        var clientIdValue = serviceAccount.getData().get(TOKEN_CLIENT_ID_KEY);
        clientIdValue = new String(Base64.getDecoder().decode(clientIdValue));

        return clientIdValue;
      }
      // We expect the token to exist, and if it doesn't raise an exception.
      throw new ConditionAwareException(String.format("Missing Service Account Secret value %s", secretName),
              null, CloudServiceCondition.Type.AcccesTokenSecretValid, CloudServiceCondition.Status.False,
              "ConditionAwareException", String.format("Missing Service Account Secret value %s", secretName));
    } catch (ConditionAwareException ex) {
      // Log and rethrow exception
      LOG.log(Level.SEVERE, ex.getMessage());
      throw ex;
    } catch (Throwable ex) {
      // Unexpected exception or error (NPE, IOException, out of memory, etc)
      LOG.log(Level.SEVERE, ex.getMessage());
      throw new ConditionAwareException(ex.getMessage(), ex,
              CloudServiceCondition.Type.Finished, CloudServiceCondition.Status.False,
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


}
