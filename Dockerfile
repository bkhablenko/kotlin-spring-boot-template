FROM eclipse-temurin:21-jre-alpine AS builder
WORKDIR /build
COPY build/libs/kotlin-spring-boot-template.jar .
RUN java -Djarmode=tools -jar kotlin-spring-boot-template.jar extract --layers --destination layers

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S appuser && adduser -S appuser -G appuser
USER appuser
WORKDIR /app
COPY --from=builder /build/layers/dependencies/ .
COPY --from=builder /build/layers/spring-boot-loader/ .
COPY --from=builder /build/layers/snapshot-dependencies/ .
COPY --from=builder /build/layers/application/ .
COPY --chmod=755 docker-entrypoint.sh .

EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=3s --start-period=30s \
    CMD wget -qO- http://localhost:9080/actuator/health || exit 1

ENTRYPOINT ["/app/docker-entrypoint.sh"]
