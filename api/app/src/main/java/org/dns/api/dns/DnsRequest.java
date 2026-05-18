package org.dns.api.dns;

import java.time.Instant;

/**
 * A record representing a DNS request.
 */
public record DnsRequest(
        String qname,
        String qtype,
        String clientIp,
        String serverIp,
        Instant timestamp) {
}
