#!/bin/sh

## First Parameter : Quay UserName
## Second Parameter : Quay Token
## Third Parameter : Quay Organization
## Fourth Parameter : Time

set -e

cd olm

echo "Quay Login"
podman login quay.io -u=$1 -p=$2

echo "Version updates ${FORCE_VERSION}"

## Uses existing version from variable or tries to autobump
if [ ! -z "${FORCE_VERSION}" ]; then
  oldnum=`cat version`  
  sed -i "s/$oldnum\$/$FORCE_VERSION/g" version
  version=$FORCE_VERSION  
else
  echo "Updating version number"
  oldnum=`cut -d '.' -f3 version`  
  newnum=`expr $oldnum + 1`
  sed -i "s/$oldnum\$/$newnum/g" version
  version=`cat version`
fi

echo "Creating bundle"
mkdir olm-catalog/rhoas-operator/$version
cp -r olm-template/* olm-catalog/rhoas-operator/$version
sed -i "s/{{version}}/$version/g" olm-catalog/rhoas-operator/$version/manifests/rhoas-operator.clusterserviceversion.yaml 
sed -i "s/{{organization}}/$3/g" olm-catalog/rhoas-operator/$version/manifests/rhoas-operator.clusterserviceversion.yaml 
sed -i "s/{{time}}/$4/g" olm-catalog/rhoas-operator/$version/manifests/rhoas-operator.clusterserviceversion.yaml 


echo "Building bundle"
podman build -f olm-catalog/rhoas-operator/$version/Dockerfile olm-catalog/rhoas-operator/$version -t quay.io/$3/service-operator-bundle:$version
podman push quay.io/$3/service-operator-bundle:$version

echo "Pushing updates back to github"
git config --local user.email "supittma+bot@redhat.com"
git config --local user.name "github-actions[bot]"
git add olm-catalog/rhoas-operator/$version
git commit -m "chore: Autobump version done by action" -a

