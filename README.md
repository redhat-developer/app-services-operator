# OpenShift Application Services Operator (RHOAS)

> Work in Progress - Kafka Operator POC

## Resources

### ManagedKafkaConnection Example

ManagedKafka Connection is being created by RHOAS CLI or manually by developer along with secret 
for purpose of making Managed Kafka appear

### Process of using ManagedKafkaConnection

1. Service Binding Operator and Managed Services Operator needs to be installed
1. Operator registers ManagedKafkaConnection
1. CLI connect command connects to current kubernetes cluster (or it can skip that)
1. CLI contacts MAS API to fetch current credentials (or uses one already created)
1. CLI contacts openshift API to create secret
1. CLI contacts openshift API to create CR
1. Manual (Service binding needs to be created on a cluster that has service binding operator and registered Managed Kafka CR)

### Examples

```
kubectl apply -f - << EOD
---
apiVersion: rhoas.redhat.com/v1
kind: ManagedKafkaConnection
metadata:
  name: kafka-managed
  namespace: your-namespace
spec:
  bootstrapServer:
    host: 'myhost.apps.openshift.com'
  credentials:
    type: ClientCredentialsSecret
    secretName: kafka-managed-credentials
EOD
```

Rereferenced secret
```
kubectl apply -f - << EOD
---
kind: Secret
apiVersion: v1
metadata:
  name: kafka-managed-credentials
data:
  clientID: YnR0ZzBqbjE3MGhw
  clientSecret: OTAwNTU5Mjc2MzI4Mjk2MQ==
type: Opaque
EOD
```

## Contributing

Check out the [Contributing Guide](./CONTRIBUTING.md) to learn more about the repository and how you can contribute.
