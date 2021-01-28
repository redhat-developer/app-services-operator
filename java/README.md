# Operator source

## Running Locally
`cd rhoas; mvn quarkus:dev`

## Building Operator Image

This will build an operator image and push it to quay.io/secondusn/rhoas-operator.

`mvn clean install -Dquarkus.container-image.build=true -Dquarkus.container-image.push=true`.

## Updating olm
Replace the image in `rhoas-operator.v0.0.1.clusterserviceversion.yaml` with your new operator image and follow the instruction in the olm readme.