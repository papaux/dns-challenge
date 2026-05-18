package org.dns.processor;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Represents an enriched DNS request after processing.
 */
public record EnrichedDnsRequest(
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")        
        Instant processingTime,
        String tld,
        int depth,
        String qtype,
        String qname,
        String serverIp,
        String clientIp
) {
}
