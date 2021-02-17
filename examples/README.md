## Mocking capability for RHOAS Operator

## Install all CRD's using kubeadmin account

```
./registerAPI.sh
```

## Interact with the operator

1. First of all we should create new project 
```
oc new-project rhoas-operator-testing
```
1. Create secret with token used to connec to the cluster
Get token from https://cloud.redhat.com/openshift/token and paste it into secret
```
oc apply -f ./secrets/api-token.yml
```
2. Create managed kafka connection 
```
oc apply -f ./crs/ManagedServicesRequest.yml
```
3. Create Managed Service Account request
```
oc apply -f ./crs/ManagedServiceAccountRequest.yml
```

4. Create connection

Go and edit `kafkaId` based on status from `ManagedServicesRequest` object

```
oc get ManagedServicesRequest -o yaml
```

Edit connection and create it
```
oc apply -f ./crs/ManagedKafkaConnection.yml
```