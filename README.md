# OpenShift Managed Application Services Operator (RHOAS)

> Work in Progress: Kafka Operator POC

## Resources

### ManagedKafkaConnection Example

```yaml
apiVersion: operators.coreos.com/v1alpha1
kind: ManagedKafkaConnection
metadata:
  name: projectKafkaConnection
spec:
  bootstrapServer:
    # This is the host name of the Kafka bootstrap
    host: foo.bar.com
  credentials:
    # Right now we only support clientCredentials, but by specifying credentials kind we allow ourselves to specify 
    # other types in the future
    kind: ClientCredentials
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

## Contributing

Check out the [Contributing Guide](./CONTRIBUTING.md) to learn more about the repository and how you can contribute.
