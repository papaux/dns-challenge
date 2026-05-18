package org.dns.api.dns;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dns-requests")
public class DnsRequestController {

    private final DnsRequestProducer producer;

    public DnsRequestController(DnsRequestProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<Void> record(@RequestBody DnsRequest request) {
        producer.send(request);
        return ResponseEntity.accepted().build();
    }
}
