# managed-services-api

This module generates an ApiClient for RHOAS managed services. The service definition is found in the file [managed-services-api.yaml](./managed-services-api.yaml). Configuration can be found in [pom.xml](./pom.xml). API generation is handled by the [openapi-generator-maven-plugin](https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-maven-plugin/README.md).

# Building
Normally this project will be built and installed as part of running `mvn install` on the parent project.

# Usage

## Maven Coordinates
```xml
<dependency>
    <groupId>com.openshift.cloud</groupId>
    <artifactId>managed-services-api</artifactId>
    <version>${project.version}</version>
</dependency>
```

## Example using bearer token

```java
ApiClient defaultClient = Configuration.getDefaultApiClient();
defaultClient.setBasePath(clientBasePath);

// Configure HTTP bearer authorization: Bearer
HttpBearerAuth Bearer = (HttpBearerAuth) defaultClient.getAuthentication("Bearer");
Bearer.setBearerToken(clientBearerToken);

var controlPanelApiClient = new DefaultApi(defaultClient);

var kafkaServiceInfo = controlPanelApiClient.getKafkaById("my-kafka-1");
```