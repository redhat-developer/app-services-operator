FROM scratch

LABEL operators.operatorframework.io.bundle.mediatype.v1=registry+v1
LABEL operators.operatorframework.io.bundle.manifests.v1=manifests/
LABEL operators.operatorframework.io.bundle.metadata.v1=metadata/
LABEL operators.operatorframework.io.bundle.package.v1=rhoas-operator
LABEL operators.operatorframework.io.bundle.channels.v1=beta
LABEL operators.operatorframework.io.bundle.channel.default.v1=beta

ADD manifests/*.yaml /manifests/
ADD metadata/annotations.yaml /metadata/annotations.yaml