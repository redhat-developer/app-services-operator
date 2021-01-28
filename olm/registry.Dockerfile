FROM quay.io/operator-framework/upstream-registry-builder as builder

COPY olm-catalog manifests
RUN ./bin/initializer -o ./bundles.db

FROM scratch
COPY --from=builder /bundles.db /bundles.db
COPY --from=builder /bin/registry-server /registry-server
COPY --from=builder /bin/grpc_health_probe /bin/grpc_health_probe
EXPOSE 50051
ENTRYPOINT ["/registry-server"]
CMD ["--database", "bundles.db"]