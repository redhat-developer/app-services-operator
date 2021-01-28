# RHOAS Operator OLM Registry
This folder contains the CRDs, catalog sources, and package definitions.

# Building

`docker build -t quay.io/secondsun/rhoas-registry:v0.0.1  -f registry.Dockerfile .`

# Pushing 

`docker push quay.io/secondsun/rhoas-registry:v0.0.1 `

# Installing
 * Be logged in as kube admin to your cluster

 `oc apply -f catalogsource.yaml`