.DEFAULT_GOAL := help
 
 
# Prints a list of useful targets.
help:
	@echo ""
	@echo "OpenShift Managed Services Operator"
	@echo ""
	@echo "make lint                 	run golangci-lint"
	@echo "make binary               	compile binaries"
	@echo "make test                 	run  tests"
	@echo "make format             		format files"
	@echo "make openapi/pull					pull openapi definition"
	@echo "make openapi/generate     	generate openapi modules"
	@echo "make openapi/validate     	validate openapi schema"

	@echo "$(fake)"
.PHONY: help

openapi/pull:
	wget -O ./openapi/managed-services-api.yaml --no-check-certificate https://gitlab.cee.redhat.com/service/managed-services-api/-/raw/master/openapi/managed-services-api.yaml
.PHONY: openapi/pull

# validate the openapi schema
openapi/validate:
	openapi-generator-cli validate -i openapi/managed-services-api.yaml
.PHONY: openapi/validate

# generate the openapi schema
openapi/generate:
	openapi-generator-cli generate -i openapi/managed-services-api.yaml -g java \
	--ignore-file-override ./.openapi-generator-ignore \
	--additional-properties=library=resteasy,apiPackage=com.openshift.cloud.api,artifactId=managedServicesApi,modelPackage=com.openshift.cloud.api.models \
	-o ./service-api 
.PHONY: openapi/generate

mock-api/start: mock-api/server/start mock-api/client/start
.PHONY: mock-api/start

mock-api/server/start:
	cd mas-mock && docker-compose up -d
.PHONY: mock-api/server/start

mock-api/client/start:
	cd mas-mock && yarn && yarn start
.PHONY: mock-api/client/start