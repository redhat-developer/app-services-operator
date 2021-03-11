FROM quay.io/operator-framework/upstream-registry-builder:v1.15.3 as builder

ARG QUAY_PASSWORD
ARG QUAY_USER
ARG QUAY_ORGANIZATION
ARG VERSION

ADD run_opm.sh .
RUN sh ./run_opm.sh $QUAY_ORGANIZATION

FROM scratch
LABEL operators.operatorframework.io.index.database.v1=/database/index.db
COPY --from=builder /bin/opm /bin/opm
COPY --from=builder database/index.db /database/index.db
COPY --from=builder /bin/grpc_health_probe /bin/grpc_health_probe

EXPOSE 50051
ENTRYPOINT ["/bin/opm"]
CMD ["registry", "serve", "--database", "/database/index.db"]

