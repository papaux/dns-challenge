# DNS Challenge

Welcome to the DNS analytics application!

It has been created to log DNS request events from an external DNS server, enrich the requests with metadata, and store for further analysis.. There are three main services which are defined in the `docker-compose.yaml` file:
1. The API for receiving DNS request events 
2. Kafka for streaming raw and enriched events
3. The processor for enriching the raw DNS request events

How it works in detail:
- A DNS server sends DNS request logs to a REST API `POST /dns-requests`. This component is not part of the code base so we can simulate it using curl commands described later.
- The `api` app deserializes the data and writes it to Kafka in the topic `dns-records`
- The `processor` app listens to the `dns-records` Kafka topic
  - For each record received, it does some enrichment (extracting TLD, counting parts, ...)
  - The output data is written to the `dns-records-analytics` Kafka topics
- Any client can registers to the `dns-records-analytics` Kafka topic to receive record stats

# Architecture Overview

```
      +-----------+
      |  client   |
      +-----+-----+
            |
            | POST /dns-requests  (JSON DnsRequest)
            v
      +-----+-----+
      |    api    |  Spring Boot, port 8081
      +-----+-----+
            |
            | produce
            v
 +==========+============+
 |  topic: dns-records   |
 +==========+============+
            |
            | consume
            v
      +-----+-----+
      | processor |  enrich: TLD, parts, ...
      +-----+-----+          
            |
            | produce
            v
 +==========+===================+
 | topic: dns-records-analytics |
 +==========+===================+
            |
            | consume (e.g. kafka-console-consumer)
            v
      +-----+-----+
      |  reader   |
      +-----------+
```

# Usage

## Start the application with docker compose

```
docker compose up --build
```

## Record a DNS request

The API exposes `POST /dns-requests` endpoints which serializes into JSON and publishes to `dns-records` Kafka topic.

The processor then consumes the topic, enriches each record, and
writes the result to `dns-records-analytics`.

```
curl -i -X POST http://localhost:8081/dns-requests \
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

## Inspecting enriched events in Kafka

Listen to the Kafka topic `dns-records-analytics`:

```
# Access the docker container running Kafka
docker exec -ti kafka /bin/bash 

# Output the live feed of events from Kafka, starting from the beginning
/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic dns-records-analytics --from-beginning
```

# Developer Documentation

## Build

## Run locally

These instructions allow to run each component independently, which is recommended when doing development or debugging.

**Start Kafka** and let it run forever while you modify other components

```
docker compose up kafka
```

**Start the API**

```
cd api
./gradlew clean build
./gradlew bootRun
```

**Start the Processor**

```
cd processor
./gradlew clean build
./gradlew bootRun
```

## Debugging

Inspect Kafka messages

```
docker exec -ti kafka /bin/bash

/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic dns-records-analytics --from-beginning
```

## Vscode web

Vscode from the web has some different shortcuts

- `F1` to open the command palette
- It is possible to "split" the terminal panel to have two (right click => split)
