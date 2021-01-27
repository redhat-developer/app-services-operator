## Namespaced
apiVersion: rhoas.redhat.com/v1alpha1
kind: ManagedKafkaRequest
metadata:
  name: namespace-name-managed-kafkas
spec:
  ## Name of the secret that stores the offline token used by the    
  ## operator to access the MAS API; defaults to 
  ## “rhoas_binding_operator_token” as per CRD
  accessTokenSecretName: rh-managed-services-api-accesstoken