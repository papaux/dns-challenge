FROM eclipse-temurin:21-jdk AS builder
ARG USER=dns
ARG USERID=1111

RUN useradd -u $USERID -m $USER
USER ${USER}

WORKDIR /app

COPY --chown=$USER:$USER . .

RUN ./gradlew clean build --no-daemon

FROM eclipse-temurin:21-jre

ARG USER=dns
ARG USERID=1111

RUN useradd -u $USERID -m $USER
USER $USER

WORKDIR /app

COPY --from=builder --chown=${USER}:${USER} /app/app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
