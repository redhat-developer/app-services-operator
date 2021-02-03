#!/bin/sh
set -e

cd olm

echo "Quay Login"
podman login quay.io -u=$1 -p=$2

echo "Updating version number"
oldnum=`cut -d '.' -f3 version`  
newnum=`expr $oldnum + 1`
sed -i "s/$oldnum\$/$newnum/g" version
version=`cat version`  

echo "Creating bundle"
mkdir olm-catalog/rhoas-operator/$version
cp -r olm-template/* olm-catalog/rhoas-operator/$version
sed -i "s/{{version}}/$version/g" olm-catalog/rhoas-operator/$version/manifests/rhoas-operator.clusterserviceversion.yaml 

echo "Building bundle"
podman build -f olm-catalog/rhoas-operator/$version/Dockerfile olm-catalog/rhoas-operator/$version -t quay.io/secondsun/rhoas-operator-bundle:$version
podman push quay.io/secondsun/rhoas-operator-bundle:$version

echo "Pushing updates back to github"
git config --local user.email "supittma.bot@redhat.com"
git config --local user.name "github-actions[bot]"
git add olm-catalog/rhoas-operator/$version
git commit -m "Autobump version" -a