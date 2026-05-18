package org.dns.processor;

import java.time.Instant;

/**
 * Represents a DNS request read from Kafka.
 */
public record DnsRequest(
        String qname,
        String qtype,
        String clientIp,
        String serverIp,
        Instant timestamp) {
}
