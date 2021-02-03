# RHOAS Operator OLM Registry
This folder contains the CRDs, catalog sources, and package definitions.

# DO NOT EDIT FILES IN THIS DIRECTORY

This folder is updated automatically by our github action. If you need to update k8s resources, you may edit or add them to the olm-template directory.

# Automatic Updates

This folder has the bundles for the csv for firepig-operator. When you commit updates to main on github, the auto build process kicks off. If you have the operator installed in your cluster, it will update there when everything gets pushed out.  The steps in the update are as follows : 
 - increment the version of the file 'version'
 - create and commit a new bundle for the new version
 - build and push the bundle
 - build a version of the operator tagged with the current version
 - push that version of the operator 
 - update the registry index image (rhoas-registry:autolatest)
 - push the registry image


# Installing
 * Be logged in as kube admin to your cluster

 `oc apply -f catalogsource.yaml`
