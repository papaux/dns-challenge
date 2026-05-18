# DNS Challenge

Welcome to this wonderful application!

It has been created to log and analyze DNS requests.

# Architecture Overview

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

/opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic dns-records-stats
```
