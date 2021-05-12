# Quickstarts

Quickstarts available as part of this operator.

## Adding Quick Starts to your OpenShift for local testing

Add all Quick Starts to your OpenShift cluster
```
oc apply -f https://raw.githubusercontent.com/redhat-developer/app-services-guides/main/devsandbox-quickstarts/rhosak-devsandbox-connect-cli-toolscontainer-quickstart.yaml
oc apply -f https://raw.githubusercontent.com/redhat-developer/app-services-guides/main/devsandbox-quickstarts/rhosak-devsandbox-getting-started-quickstart.yaml
oc apply -f https://raw.githubusercontent.com/redhat-developer/app-services-guides/main/devsandbox-quickstarts/rhosak-devsandbox-kafkacat-toolscontainer-quickstart.yaml
oc apply -f https://raw.githubusercontent.com/redhat-developer/app-services-guides/main/devsandbox-quickstarts/rhosak-devsandbox-quarkus-bind-cli-toolscontainer-quickstart.yaml
```

After installation go to right top corner `?` icon and select `Quickstarts` to view new Quickstarts that were added.

## Contributing Quick Starts

To contribute quickstarts, follow the
[guidelines](http://openshift.github.io/openshift-origin-design/conventions/documentation/quick-starts.html)
for writing a quick start and getting the content reviewed. When the
quick start is ready, add the quick start YAML to this folder and open a PR.

