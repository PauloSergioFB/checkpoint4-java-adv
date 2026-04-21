package br.com.fiap.checkpoint4.messaging;

import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.checkpoint4.presentation.transferObjects.Event;
import br.com.fiap.checkpoint4.presentation.transferObjects.OrderDTO;

@Component
@RestController
@RequestMapping("/api/v1/orders")
public class OrderProducer {

    private final JmsTemplate queueJmsTemplate;

    public OrderProducer(JmsTemplate queueJmsTemplate) {
        this.queueJmsTemplate = queueJmsTemplate;
    }

    @PostMapping
    public ResponseEntity<String> send(@RequestBody OrderDTO orderDTO) {
        System.out.println("\nProducer: Pedido recebido " + orderDTO);

        Event event = new Event(orderDTO, 0, null); // Inclusão de metadados na mensagem
        System.out.println("Producer: Pedido enviado para order.receiver.queue");
        queueJmsTemplate.convertAndSend("order.receiver.queue", event);

        return ResponseEntity.ok("ok");
    }

}
