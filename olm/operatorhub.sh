## Script for manual release to operator hub

# git clone git@github.com:operator-framework/community-operators.git
cp -Rf ./olm-catalog/rhoas-operator/`cat version` ./community-operators/community-operators/rhoas-operator

cd ./community-operators && git add --all \
    && git commit -a -m"RHOAS Operator version `cat version`"