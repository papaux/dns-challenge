# DNS Challenge

Welcome to this wonderful application!

It has been created to log and analyze DNS requests.

# Architecture Overview

# Usage

## Start the application with docker compose

```
docker compose up --build
```

## Recording a DNS request

The API exposes `POST /dns-requests` endpoints which serializes into JSON and publishes to `dns-records` Kafka topic. 

The processor then consumes the topic, enriches each record, and
writes the result to `dns-records-analytics`.

```
curl -i -X POST http://localhost:8080/dns-requests \
  -H 'Content-Type: application/json' \
  -d '{
    "qname": "example.com",
    "qtype": "A",
    "clientIp": "192.168.1.10",
    "serverIp": "8.8.8.8",
    "timestamp": "2026-05-18T12:34:56Z"
  }'
```

A successful call returns `202 Accepted`.

## Inspecting the output

Listen to the Kafka topic `dns-records-analytics`:

```
docker exec -ti kafka /bin/bash

/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic dns-records-analytics --from-beginning
```


# Developer Documentation

## Build

## Run locally

These instructions allow to run each component independently.

**Start Kafka**
```
docker compose up kafka
```

**Start the API**

TODO

**Start the Processor**

TODO


## Debugging

Inspect Kafka messages

```
docker exec -ti kafka /bin/bash

/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic dns-records-analytics --from-beginning
```
