FROM eclipse-temurin:17-jre-alpine

# https://github.com/open-telemetry/opentelemetry-java/blob/main/sdk-extensions/autoconfigure/README.md
ENV OTEL_SERVICE_NAME=kotlin-spring-boot-template
ENV OTEL_METRICS_EXPORTER=none

ARG JAR_FILE="./build/libs/kotlin-spring-boot-template-0.1.0-SNAPSHOT.jar"

RUN apk update && \
    apk add --no-cache curl && \
    curl -sL https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.29.0/opentelemetry-javaagent.jar -o /opentelemetry-javaagent.jar && \
    adduser -D appuser

USER appuser

COPY "$JAR_FILE" /app.jar
COPY --chmod=0755 docker-entrypoint.sh /docker-entrypoint.sh

HEALTHCHECK --interval=10s --timeout=5s --start-period=30s \
    CMD curl -f http://localhost:8080/health || exit 1

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh"]
