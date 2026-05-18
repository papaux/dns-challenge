package org.dns.api.dns;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * A record representing a DNS request.
 */
public record DnsRequest(
        String qname,
        String qtype,
        String clientIp,
        String serverIp,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        Instant timestamp) {
}
