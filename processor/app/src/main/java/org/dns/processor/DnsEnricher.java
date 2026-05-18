package org.dns.processor;

import java.time.Instant;

import org.springframework.stereotype.Component;

/**
 * Enriches a {@link DnsRequest} with metadata useful for downstream analytics.
 */
@Component
public class DnsEnricher {

    public EnrichedDnsRequest enrich(DnsRequest request) {
        String qname = stripTrailingDot(request.qname());
        String[] parts = qname.isEmpty() ? new String[0] : qname.split("\\.");

        String tld = parts.length > 0 ? parts[parts.length - 1] : "";
        int depth = parts.length;

        return new EnrichedDnsRequest(
                Instant.now(),
                request.timestamp(),
                tld,
                depth,
                request.qtype(),
                request.qname(),
                request.serverIp(),
                request.clientIp()
            );
    }

    private String stripTrailingDot(String qname) {
        if (qname == null) {
            return "";
        }
        return qname.endsWith(".") ? qname.substring(0, qname.length() - 1) : qname;
    }
}
