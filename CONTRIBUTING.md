# RHOAS Operator

## Getting started 

### Prerequisites

 - Java 11+
 - mvn
 - OpenShift Client (or kubernetes)

### Running Operator

1. Login to your OpenShift cluster 
`oc login`

2. Install dependencies
```
mvn clean install
```

3. Run operator

`cd source/rhoas; mvn quarkus:dev`

3. See ./examples folder for example CR's workflow that should be used to test operator

## Folder structure

```
├── api-mock   contains service api mocks
├── docs       documentation
├── examples   CR examples for testing
├── olm        OLM configuration for deployment
└── source     Java packages
```

Java Source packages
```
    ├── model           CR model files used to call kubernetes api
    ├── openapi         Contains POJOS that conform to OpenAPI spec
    ├── rhoas           API controllers

```


## Building Operator Image

This will build an operator image and push it to quay.io/secondusn/rhoas-operator.

`mvn clean install -Dquarkus.container-image.build=true -Dquarkus.container-image.push=true`.

## Updating olm images

Replace the image in `rhoas-operator.v0.0.1.clusterserviceversion.yaml` with your new operator image and follow the instruction in the olm readme.