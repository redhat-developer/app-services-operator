## Sample ManagedKafkaConnection

```yaml
apiVersion: operators.coreos.com/v1alpha1
kind: ManagedKafkaConnection
metadata:
  name: myKafkaConnection
spec:
  bootstrapServer:
    # This is the host name of the Kafka bootstrap
    host: foo.bar.com
  credentials:
    # Right now we only support clientCredentials, but by specifying credentials kind we allow ourselves to specify 
    # other types in the future
    kind: clientCredentials
    # The actual credential values 
    clientId:
      # Values can appear in the custom resource
      value: abc123
    clientSecret:
      # Values can be a reference to a secret, configMap etc.
      valueFrom:
        key: clientSecret
        name: managedKafkaSecret
```
