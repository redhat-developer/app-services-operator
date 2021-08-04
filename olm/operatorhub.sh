## Script for manual release to operator hub
## After script finishes please manually 

 git clone git@github.com:redhat-openshift-ecosystem/community-operators-prod.git
VERSION=`cat version`
mkdir ./community-operators-prod/operators/rhoas-operator/${VERSION}
cp -Rf ./olm-catalog/rhoas-operator/${VERSION}/manifests ./community-operators-prod/operators/rhoas-operator/${VERSION}
cp -Rf ./olm-catalog/rhoas-operator/${VERSION}/metadata ./community-operators-prod/operators/rhoas-operator/${VERSION}

cd ./community-operators-prod && \
    git checkout -b rhoas-operator-${VERSION}  && \
    git add --all && \
    git commit --signoff -m"chore: update rhoas operator ${VERSION}"
