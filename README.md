# DNS Challenge

Welcome to the DNS analytics application!

It has been created to log and analyze DNS requests.

How it works:
- A DNS server sends DNS request logs to a REST API `POST /dns-requests`
- The `api` app deserializes the data and writes it to Kafka in the topic `dns-records`
- The `processor` app listens to the `dns-records` Kafka topic
  - For each record received, it does some enrichment (extracting TLD, counting parts, ...)
  - The output data is written to the `dns-record-analytics` Kafka topics
- Any client can registers to the `dns-record-analytics` Kafka topic to receive record stats

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
