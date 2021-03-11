# RHOAS Service-Binding Operator
This operator creates and manages custom resources used by the RHOAS Kafka Service.

# Prerequesites
 * Java 11+
 * Maven
 * A service-api token from cloud.redhat.com (Talk to Pete)
 * Access to a RHOAS services .

# Building and Running

## TL;DR;

```sh
mvn clean install;
cd rhoas;
mvn quarkus:dev;
```

## Building

The operator source is in the [rhoas](./rhoas) module. The [openapi](./openapi) module generates an API based on the managed-services-api openapi manifest. `rhoas` has a dependency on `openapi` and therefor the parent project should be installed with `mvn clean install` before the operator is run.

```sh
mvn clean install;
```

## Running in dev mode

The operator uses [quarkus](https://quarkus.io) and the [java-operator-sdk](https://github.com/java-operator-sdk/java-operator-sdk).

You can start the operator in dev mode.

```sh
mvn quarkus:dev
```

# Usage

Once the operator has come online it will load deployed crds and deploy crds based on our model classes if no deployed crds are found. You can verify the operator is working by creating a `KafkaConnection` custom resource. And example follows :

```yaml
apiVersion: rhoas.redhat.com/v1alpha1
kind: KafkaConnection
metadata:
  name: rhosak-demo

spec:
  kafkaId: 1mk2CcqHByUJCg0sZxiGv8f53Pz
```

