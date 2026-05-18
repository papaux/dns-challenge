FROM eclipse-temurin:21-jdk AS builder
ARG USER=dns
ARG USERID=1111

RUN useradd -u $USERID -m $USER
USER ${USER}

WORKDIR /app

COPY --chown=$USER:$USER . .

RUN --mount=type=cache,target=/home/${USER}/.gradle,uid=${USERID} \
    ./gradlew clean build --no-daemon

FROM eclipse-temurin:21-jre

ARG USER=dns
ARG USERID=1111

RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    iputils-ping \
    && rm -rf /var/lib/apt/lists/*

RUN useradd -u $USERID -m $USER
USER $USER

WORKDIR /app

COPY --from=builder --chown=${USER}:${USER} /app/app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
