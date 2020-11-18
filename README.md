## Sample ManagedKafkaConnection

```yaml
kind: ManagedKafkaConnection
spec:
  bootstrapServer:
    host: foo.bar.com
  credentials:
    kind: clientCredentials
    clientId:
      valueFrom:
        key: clientId
        name: managedKafkaSecret
    clientSecret:
      valueFrom:
        key: clientSecret
        name: managedKafkaSecret
```
