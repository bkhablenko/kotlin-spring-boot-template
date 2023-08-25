#!/bin/sh

java \
  -javaagent:/opentelemetry-javaagent.jar \
  -Duser.timezone=UTC \
  -Xms1g \
  -Xmx4g \
  -jar /app.jar
