package org.dns.processor;

import java.time.Instant;

/**
 * Represents an enriched DNS request after processing.
 */
public record EnrichedDnsRequest(
        Instant processingTime,
        String tld,
        int depth,
        String qtype,
        String qname,
        String serverIp,
        String clientIp
) {
}
