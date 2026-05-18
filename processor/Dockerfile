FROM  registry.redhat.io/ubi9/openjdk-21:1.24-2.1778501191 AS builder
ARG USER 185

WORKDIR /app

COPY --chown=$USER:$USER . .

RUN ./gradlew clean build --no-daemon

FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24

WORKDIR /app

COPY --from=builder /app/app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
