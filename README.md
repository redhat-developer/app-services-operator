# Managed Kafka Operator POC

## Running Operator:

1. Regenerate operator and resources
   `make generate`
   `make manifests`
  
2. Install operator

  `make install`

3. Disable webhooks `make run ENABLE_WEBHOOKS=false`
see [operator-sdk documentation](https://sdk.operatorframework.io/docs/building-operators/golang/tutorial/) for further info

## Resources

### ManagedKafkaConnection

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
