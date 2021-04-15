# OpenShift Application Services Connection Operator (RHOAS-Operator)

OpenShift Application Services Connection Operator (RHOAS-Operator) manages the connections between RHOAS Service Accounts and RHOAS Cloud Services. Users provide RHOAS-Operator with a [cloud.redhat.com](https://cloud.redhat.com) API Token via a secret and the operator uses that token to access the Managed Services APIs on behalf of the user to process their KafkaConnection, CloudServicesRequest, and CloudServiceAccountRequest custom resources. These resources are monitored by the [Service Binding Operator](https://github.com/redhat-developer/service-binding-operator) which provides connectivity between your application and RHOAS services.

## Related Projects

 * [app services cli](https://github.com/redhat-developer/app-services-cli)
 * [app services console plugin](https://github.com/redhat-developer/app-services-openshift-console-plugin)

## Documentation

[Documentation](./docs)

## Contributing

Check out the [Contributing Guide](./CONTRIBUTING.adoc) to learn more about the repository and how you can contribute.
