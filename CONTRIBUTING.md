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

operator-sdk init --domain=redhat.com --plugins=go.kubebuilder.io/v2 --project-name=rhoas-operator --repo=github.com/bf2fc6cc711aee1a0c2a/operator

2. Create API (without controller)
```
operator-sdk create api --version=v1 --group=rhoas --kind=ManagedKafkaConnection --namespaced=false --resource=true --controller=false
```

3. Add controller to your API if needed (later phase)

```
operator-sdk create api --version=v1 --group=rhoas --kind=ManagedKafkaConnection --namespaced=false --resource=false --controller=true
```

## Testing using CRDs

Applying generated API to kubernetes

```
make install
make run ENABLE_WEBHOOKS=false
```

Create sample resource
```
oc apply -f config/samples/rhoas_v1_managedkafkaconnection.yaml 
```

Check if resource was created

```
 oc get crd managedkafkaconnections.rhoas.redhat.com -o yaml
```

### Testing Service Binding

1. Follow steps in

https://github.com/redhat-developer/service-binding-operator/blob/master/examples/nodejs_postgresql/README.md


3. Review binding info and apply it
```
oc apply -f ./hack/binding-example.yaml
```

4. Check binding status
```
oc get servicebinding managed-kafka-binding-request -o yaml
```

## Installing Operator Using Catalog

```
kubectl apply -f - << EOD
---
apiVersion: operators.coreos.com/v1alpha1
kind: CatalogSource
metadata:
    name: managed-kafka-operator-deployment
    namespace: openshift-marketplace
spec:
    sourceType: grpc
    image: quay.io/wtrocki/9ed633bb57ee71f8865ba7a442a73c06
    displayName: Mananaged Kafka Operator deployment
EOD
```