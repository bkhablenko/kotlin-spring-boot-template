#!/usr/bin/env sh

exec java \
  -javaagent:/opentelemetry-javaagent.jar \
  -Duser.timezone=UTC \
  -XX:+ExitOnOutOfMemoryError \
  -XX:MaxRAMPercentage=75.0 \
  -XX:InitialRAMPercentage=50.0 \
  -jar /app/kotlin-spring-boot-template.jar "$@"
