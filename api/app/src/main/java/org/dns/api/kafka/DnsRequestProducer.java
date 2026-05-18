package org.dns.api.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.dns.api.dns.DnsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DnsRequestProducer {

    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;
    private final String topic;

    public DnsRequestProducer(
            KafkaProducer<String, String> producer,
            ObjectMapper objectMapper,
            @Value("${kafka.output-topic}") String topic) {
        this.producer = producer;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    public void send(DnsRequest request) {
        try {
            String payload = objectMapper.writeValueAsString(request);
            producer.send(new ProducerRecord<>(topic, request.qname(), payload));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize DnsRequest", e);
        }
    }
}
