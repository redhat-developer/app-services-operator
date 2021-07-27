
<a name="0.7.7"></a>
## [0.7.7](https://github.com/redhat-developer/app-services-operator/compare/0.7.6...0.7.7) (2021-07-19)

### Features

* install quickstarts on boot


<a name="0.7.6"></a>
## [0.7.6](https://github.com/redhat-developer/app-services-operator/compare/0.7.5...0.7.6) (2021-06-24)

### Features

* using new api package
* migration to app-services-sdk


<a name="0.7.5"></a>
## [0.7.5](https://github.com/redhat-developer/app-services-operator/compare/0.7.4...0.7.5) (2021-06-16)


<a name="0.7.4"></a>
## [0.7.4](https://github.com/redhat-developer/app-services-operator/compare/0.7.3...0.7.4) (2021-06-14)


<a name="0.7.3"></a>
## [0.7.3](https://github.com/redhat-developer/app-services-operator/compare/0.7.2...0.7.3) (2021-06-07)

### Bug Fixes

* publishing openapi and fixing generation
* Remove invalid workflow
* add website build
* add missing README to quickstarts

### Documentation

* Updates to Operator installation procedure ([#221](https://github.com/redhat-developer/app-services-operator/issues/221))
* Added quick starts initially created for dev sandbox to the operator, so they will be automatically installed on any OpenShift instance on which the rhoas operator is installed. ([#215](https://github.com/redhat-developer/app-services-operator/issues/215))


<a name="0.7.2"></a>
## [0.7.2](https://github.com/redhat-developer/app-services-operator/compare/0.7.1...0.7.2) (2021-05-04)

### Bug Fixes

* change extensively long operator name ([#209](https://github.com/redhat-developer/app-services-operator/issues/209))
* update open api spec ([#206](https://github.com/redhat-developer/app-services-operator/issues/206))
* name of the project and general information
* provide operator hub instllation docs ([#200](https://github.com/redhat-developer/app-services-operator/issues/200))
* add reference to service binding operator
* update readme with shorter into
* push tags for release process
* update to trigger builds only on release


<a name="0.7.1"></a>
## [0.7.1](https://github.com/redhat-developer/app-services-operator/compare/0.7.0...0.7.1) (2021-04-21)

### Bug Fixes

* add logging capability to tell us what env is used by operator
* add explicit env variable pointing to the environment


<a name="0.7.0"></a>
## [0.7.0](https://github.com/redhat-developer/app-services-operator/compare/0.6.9...0.7.0) (2021-04-21)

### Bug Fixes

* update default url to production ([#178](https://github.com/redhat-developer/app-services-operator/issues/178))


<a name="0.6.9"></a>
## [0.6.9](https://github.com/redhat-developer/app-services-operator/compare/0.6.8...0.6.9) (2021-04-21)

### Bug Fixes

* update case for KafkaConnect unit tests ([#187](https://github.com/redhat-developer/app-services-operator/issues/187))
* url for the kafka in the UI
* change email in olm
* add information to description about limited beta


<a name="0.6.8"></a>
## [0.6.8](https://github.com/redhat-developer/app-services-operator/compare/0.6.7...0.6.8) (2021-04-15)

### Bug Fixes

* improve error handling to include message from server


<a name="0.6.7"></a>
## [0.6.7](https://github.com/redhat-developer/app-services-operator/compare/0.6.6...0.6.7) (2021-04-13)

### Bug Fixes

* add lowercase versions
* minor issue with OLM spec


<a name="0.6.6"></a>
## [0.6.6](https://github.com/redhat-developer/app-services-operator/compare/0.6.5...0.6.6) (2021-04-09)

### Bug Fixes

* change references to the organisation
* add OAuth Bearer and SASL PLAIN binding support


<a name="0.6.5"></a>
## [0.6.5](https://github.com/redhat-developer/app-services-operator/compare/0.6.4...0.6.5) (2021-04-01)

### Bug Fixes

* Add NPE protections to the operator with proper validation messsages


<a name="0.6.4"></a>
## [0.6.4](https://github.com/redhat-developer/app-services-operator/compare/0.6.3...0.6.4) (2021-03-30)

### Bug Fixes

* userKafkas Update, removing broken equals impl
* catching uncaught exceptions and setting status on finished
* removing memory limits
* update olm descriptions based on the requirements


<a name="0.6.3"></a>
## [0.6.3](https://github.com/redhat-developer/app-services-operator/compare/0.6.2...0.6.3) (2021-03-23)

### Bug Fixes

* improve error messages for error handling
* add missing  x-kubernetes-preserve-unknown-fields to service accounts request


<a name="0.6.2"></a>
## [0.6.2](https://github.com/redhat-developer/app-services-operator/compare/0.6.1...0.6.2) (2021-03-23)

### Bug Fixes

* add extra 503 handling
* add extra 503 handling


<a name="0.6.1"></a>
## [0.6.1](https://github.com/redhat-developer/app-services-operator/compare/0.6.0...0.6.1) (2021-03-18)

### Features

* initial test class and config


<a name="0.6.0"></a>
## [0.6.0](https://github.com/redhat-developer/app-services-operator/compare/0.5.1...0.6.0) (2021-03-17)

### Bug Fixes

* minor typo causing an NPE
* actually checking finished status now...
* catching wider error type
* catching wider error type

### Features

* refactoring controllers to put retry and condition handling in outer structure
* adding last generation to conditions


<a name="0.5.1"></a>
## [0.5.1](https://github.com/redhat-developer/app-services-operator/compare/0.5.0...0.5.1) (2021-03-16)

### Bug Fixes

* add autoignore variables ([#137](https://github.com/redhat-developer/app-services-operator/issues/137))


<a name="0.5.0"></a>
## [0.5.0](https://github.com/redhat-developer/app-services-operator/compare/0.4.0...0.5.0) (2021-03-16)

### Bug Fixes

* Add ability to preserve custom fields for status of every object.


<a name="0.4.0"></a>
## [0.4.0](https://github.com/redhat-developer/app-services-operator/compare/0.3.1...0.4.0) (2021-03-15)

### BREAKING CHANGES

* Add metadata container for the KafkaConnection

### Bug Fixes

* remove dekorate integration


<a name="0.3.1"></a>
## [0.3.1](https://github.com/redhat-developer/app-services-operator/compare/0.3.0...0.3.1) (2021-03-12)

### Bug Fixes

* delete outdated api mock ([#127](https://github.com/redhat-developer/app-services-operator/issues/127))

### Features

* memory limits, updated icon, minor fixes and tweaks, autoupdate ([#126](https://github.com/redhat-developer/app-services-operator/issues/126))


<a name="0.3.0"></a>
## [0.3.0](https://github.com/redhat-developer/app-services-operator/compare/0.2.1...0.3.0) (2021-03-11)

### BREAKING CHANGES

* Replace resources to remove "managed" part. ([#122](https://github.com/redhat-developer/app-services-operator/issues/122))

### Bug Fixes

* minor changes
* Adding resource limits to CSV
* updates

### Features

* Quarkus 1.12.0 and SDK 1.7.4


<a name="0.2.1"></a>
## [0.2.1](https://github.com/redhat-developer/app-services-operator/compare/0.2.0...0.2.1) (2021-03-03)

### Bug Fixes

* uiRef got lost in merge and typo

### Features

* adding a new uiRef status to MKC


<a name="0.2.0"></a>
## [0.2.0](https://github.com/redhat-developer/app-services-operator/compare/0.1.1...0.2.0) (2021-03-03)

### Bug Fixes

* move from plaintext to plain sasl mechanism
* swap SASL values passed for the status
* delete unneeded folder


<a name="0.1.1"></a>
## [0.1.1](https://github.com/redhat-developer/app-services-operator/compare/0.1.0...0.1.1) (2021-03-01)

### Bug Fixes

* order of the service binding definitions


<a name="0.1.0"></a>
## [0.1.0](https://github.com/redhat-developer/app-services-operator/compare/golang...0.1.0) (2021-03-01)

### BREAKING CHANGES

* bootstrapServerHost as root value
* change API for managed services requests

### Bug Fixes

* typo in bootstrapServerHost ([#95](https://github.com/redhat-developer/app-services-operator/issues/95))
* bash script inconsistency
* fix olm script for if statement ([#88](https://github.com/redhat-developer/app-services-operator/issues/88))
* imports in java code
* Update to the official quay images and references
* allow to release exact versions and split into prerelease and release flows
* delete invalid builds from master
*  all namespaces watched by default, removing
* update sdk
* minor changes
* backwards compatability for old type name
* tests were not running. Marking stubs as disabled
* renaming condition and forcing finished to false
* updating crds to v1 crd definition
* leave non breaking manifest on the top
* invalid abbreviation
* update readme comment
* rollback operator-sdk version
* Update binding spec
* fixing connection controller
* update binding spec
* update binding configuration
* add service binding annotations and additional fields ([#49](https://github.com/redhat-developer/app-services-operator/issues/49))
* remove reset field
* add contributing
* remove not needed examples folder
* add additional properties required by UI to the kafka list and change format to array instead of map
* remove complexity around date format
* move examples to the root folder
* simplify development by using latest for the registry and controller image
* rename modules to source
* update image version and catalog registry
* add java specific build
* module configuration
* Improve contributing guide
* just bunch of renames
* change description field to better name
* Description of the operator in OLM
* major bug fixes
* wip
* small scheduler change
* removing unused crds
* use constant for secret
* duplicate value
* minor readme updates
* remove golang codebase
* change quarkus port
* adding mocking and readme
* adding offline token to serviceaccountrequestspec

### Features

* maybe conditions
* conditions
* automatic OLM updates
* olm "works"
* refacotring to use dekorate
* managedsarequest
* wip managedserviceaccountcontroller


<a name="golang"></a>
## golang (2021-01-21)

### Bug Fixes

* move to operator in order to registrer resources
* add api mock
* correcting usage of update control
* fixing build failures
* CRD draft
* resources
* improved types
* CRD draft
* generator setup
* generated API
* api changes
* minor fixes
* add base for operator
* add documentation for resource cleanup
* remove invalid link
* delete not needed file
* Adding basic documentation
* add permissions for CR
* add wider permissions for finalizers
* add push index image
* finalizers update permissions
* add secrets rbac
* group for deployments in rbac control
* update service version
* add info about indexing
* removal procedure
* add additional catalog source elements
* Update image and makefile
* package manifests
* remove replaces
* change bundle
* update catalog source
* change version
* swap images
* bundle details
* disable login
* use secret first
* add metadata
* add more information to
* setup image name
* add arch to builds
* add extreamly complex container
* update docs
* Dependency game
* Updated version of the controller
* add dummy deployment for testing
* Cleanup in operator (use rhoas as name)
* add status changes
* update readme
* reflect values for CLI POC
* remove namespace
* improve documentation
* metadata update
* add kustomization
* bundle
* minor improvements
* add annotations
* add managed kafka instalation instruction
* add controllers folder to finalize docker build
* adding binding example
* add useful commands
* use temporary image
* add binding metadata
* formatting for the types
* minor fixes for the operator
* add extra comments for clarity
* rename title
* add build support
* Update CRD
* Update CRD
* Add basic readme
* initial template for the operator

### Features

* offline token exchange
* managedkafkarequest controller works
* refreshing mkrequest
* MKRequest Controller
* wip managedkafkaresponse
* base path and token are configurable
* wip
* k8sclient, upgrade operatorsdk, upgrad quarks
* openapi generated by Java

