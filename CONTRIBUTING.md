## Running Operator:

1. Regenerate operator and resources
   `make generate`
   `make manifests`
  
2. Install operator

  `make install`

3. Disable webhooks `make run ENABLE_WEBHOOKS=false`
see [operator-sdk documentation](https://sdk.operatorframework.io/docs/building-operators/golang/tutorial/) for further info

## Commands Used to Create Operator

Operator has muliple names embeeded that we need to clarify
Any rename will possibly mean changes in multiple places.
Renames on this project should be handled by recreating project completely.


> NOTE: Make sure you have latest version of the Operator-SDK CLI (used 1.2)

1. Init operator

operator-sdk init --domain=redhat.com --plugins=go.kubebuilder.io/v2 --project-name=oas-operator --repo=github.com/bf2fc6cc711aee1a0c2a/operator

2. Create API (without controller)
```
operator-sdk create api --version=v1 --group=oas --kind=ManagedKafkaConnection --namespaced=false --resource=true --controller=false
```

3. Add controller to your API if needed (later phase)

```
operator-sdk create api --version=v1 --group=oas --kind=ManagedKafkaConnection --namespaced=false --resource=false --controller=true
```

## Testing CRD only

Applying generated API to kubernetes

oc apply -f config/crd/bases/oas.redhat.com_managedkafkaconnections.yaml =
oc apply -f config/samples/oas_v1_managedkafkaconnection.yaml 

## Reaplying binding metadata

Required values in metadata

```
  service.binding/host: 'path={.spec.bootstrapServer.host}'
  service.binding/user: 'path={.spec.credentials.clientID}'
  service.binding/password: 'path={.spec.credentials.clientSecret}'
  service.binding/secret: 'path={.spec.credentials.clientSecret},objectType=Secret'
```