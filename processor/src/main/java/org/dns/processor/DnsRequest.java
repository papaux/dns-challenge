package org.dns.processor;

/**
 * Represents a DNS request read from Kafka.
 */
public record DnsRequest(
        String qname,
        String qtype,
        String clientIp,
        String serverIp,
        Long timestamp) {
}
