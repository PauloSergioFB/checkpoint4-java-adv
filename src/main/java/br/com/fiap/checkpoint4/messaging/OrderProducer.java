package br.com.fiap.checkpoint4.messaging;

import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.checkpoint4.presentation.transferObjects.Event;
import br.com.fiap.checkpoint4.presentation.transferObjects.OrderDTO;

@Component
@RestController
@RequestMapping("/api/v/orders")
public class OrderProducer {

    private final JmsTemplate queueJmsTemplate;

    public OrderProducer(JmsTemplate queueJmsTemplate) {
        this.queueJmsTemplate = queueJmsTemplate;
    }

    @GetMapping
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Ok");
    }

    @PostMapping
    public ResponseEntity<String> send(@RequestBody OrderDTO orderDTO) {
        Event event = new Event(orderDTO, 0, null);
        this.queueJmsTemplate.convertAndSend("order.receiver.queue", event);
        // System.out.println("QUEUE recebeu: " + event.payload()); // enviado
        return ResponseEntity.ok("ok");
    }

}
