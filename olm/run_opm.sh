#!/bin/bash



has_image () {
  podman manifest inspect quay.io/$1/service-operator-registry:autolatest > /dev/null; 
}

if  has_image ; then 
  opm index add --bundles quay.io/$1/service-operator-bundle:$VERSION  --mode=semver --generate -f quay.io/$1/service-operator-registry:autolatest
else 
  opm index add --bundles quay.io/$1/service-operator-bundle:$VERSION  --mode=semver --generate
fi
