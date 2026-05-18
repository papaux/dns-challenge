package org.dns.api.dns;

import java.time.Instant;

public record DnsRequest(
        String qname,
        String qtype,
        String clientIp,
        String serverIp,
        Instant timestamp) {
}
