FROM eclipse-temurin:21-jre-alpine AS builder
WORKDIR /build
COPY build/libs/kotlin-spring-boot-template.jar .
RUN java -Djarmode=tools -jar kotlin-spring-boot-template.jar extract --layers --destination layers

FROM eclipse-temurin:21-jre-alpine

# https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md
ENV OTEL_SERVICE_NAME=kotlin-spring-boot-template
ENV OTEL_METRICS_EXPORTER=none

RUN wget -qO /opentelemetry-javaagent.jar \
      https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.29.0/opentelemetry-javaagent.jar && \
    addgroup -S appuser && adduser -S appuser -G appuser

USER appuser
WORKDIR /app
COPY --from=builder /build/layers/dependencies/ .
COPY --from=builder /build/layers/spring-boot-loader/ .
COPY --from=builder /build/layers/snapshot-dependencies/ .
COPY --from=builder /build/layers/application/ .
COPY --chmod=755 docker-entrypoint.sh .

EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=3s --start-period=30s \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["/app/docker-entrypoint.sh"]
