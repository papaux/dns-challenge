package org.dns.processor;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Reads DNS records produced by the API, enriches them, and writes them back to Kafka.
 *
 * <p>The poll/enrich/produce loop runs on a dedicated non-daemon thread so the Spring
 * Boot application stays alive as a long-running stream processor.
 */
@Component
public class DnsRecordsStreamer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DnsRecordsStreamer.class);
    private static final String CONSUMER_GROUP_ID = "dns-records-analytics";

    private final String bootstrapServers;
    private final String inputTopic;
    private final String outputTopic;
    private final DnsEnricher enricher;
    private final ObjectMapper objectMapper;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public DnsRecordsStreamer(
            @Value("${kafka.bootstrap-servers}") String bootstrapServers,
            @Value("${kafka.input-topic}") String inputTopic,
            @Value("${kafka.output-topic}") String outputTopic,
            DnsEnricher enricher,
            ObjectMapper objectMapper) {
        this.bootstrapServers = bootstrapServers;
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
        this.enricher = enricher;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) {
        Thread worker = new Thread(this::pollLoop, "dns-records-streamer");
        worker.setDaemon(false);
        worker.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> running.set(false)));
    }

    private void pollLoop() {
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps());
                KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps())) {
            consumer.subscribe(List.of(inputTopic));
            LOGGER.info("Subscribed to {}, forwarding enriched records to {}", inputTopic, outputTopic);
            while (running.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    processRecord(record, producer);
                }
                consumer.commitSync();
            }
        } catch (Exception e) {
            LOGGER.error("DNS records streamer terminated unexpectedly", e);
        }
    }

    private void processRecord(ConsumerRecord<String, String> record, KafkaProducer<String, String> producer) {
        try {
            DnsRequest request = objectMapper.readValue(record.value(), DnsRequest.class);
            EnrichedDnsRequest enriched = enricher.enrich(request);
            String payload = objectMapper.writeValueAsString(enriched);
            producer.send(new ProducerRecord<>(outputTopic, record.key(), payload));
        } catch (Exception e) {
            LOGGER.warn("Skipping record offset={} value={}", record.offset(), record.value(), e);
        }
    }

    private Properties consumerProps() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    private Properties producerProps() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        return props;
    }
}
