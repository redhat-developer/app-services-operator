# Quickstarts

Quickstarts available as part of this operator.

## Adding Quick Starts to your OpenShift for local testing

The operator automatically installs quickstarts when it starts up. If you wish to replace them, you may use the following : 
```
oc apply -f https://raw.githubusercontent.com/redhat-developer/app-services-operator/quickstarts/olm/quickstarts/rhosak-openshift-connect-quickstart.yaml
oc apply -f https://raw.githubusercontent.com/redhat-developer/app-services-operator/quickstarts/olm/quickstarts/rhosak-openshift-getting-started-quickstart.yaml
oc apply -f https://raw.githubusercontent.com/redhat-developer/app-services-operator/quickstarts/olm/quickstarts/rhosak-openshift-kafkacat-quickstart.yaml
oc apply -f https://raw.githubusercontent.com/redhat-developer/app-services-operator/quickstarts/olm/quickstarts/rhosak-openshift-quarkus-bind-quickstart.yaml
```

After installation go to right top corner `?` icon and select `Quickstarts` to view new Quickstarts that were added.

## Contributing Quick Starts

To contribute quickstarts, follow the
[guidelines](http://openshift.github.io/openshift-origin-design/conventions/documentation/quick-starts.html)
for writing a quick start and getting the content reviewed. When the
quick start is ready, add the quick start YAML to this folder and open a PR.

