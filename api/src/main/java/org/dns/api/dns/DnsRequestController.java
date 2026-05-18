package org.dns.api.dns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dns-requests")
public class DnsRequestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DnsRequestController.class);
 
    private final DnsRequestProducer producer;

    public DnsRequestController(DnsRequestProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<Void> record(@RequestBody DnsRequest request) {
        LOGGER.info("Received DNS request: {}", request);
        producer.send(request);
        return ResponseEntity.accepted().build();
    }
}
