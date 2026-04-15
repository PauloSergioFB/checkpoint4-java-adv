package br.com.fiap.checkpoint4.messaging;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.checkpoint4.presentation.transferObjects.OrderDTO;

@Component
@RestController
@RequestMapping("/api/v/orders")
public class OrderProducer {

    private final JmsTemplate queueJmsTemplate;

    public OrderProducer(
            @Qualifier("queueJmsTemplate") JmsTemplate queueJmsTemplate) {
        this.queueJmsTemplate = queueJmsTemplate;
    }

    @GetMapping
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Ok");
    }

    @PostMapping
    public ResponseEntity<String> send(@RequestBody OrderDTO orderDTO) {
        this.queueJmsTemplate.convertAndSend("order.receiver.queue", orderDTO);
        return ResponseEntity.ok("ok");
    }

}
