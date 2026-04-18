package br.com.fiap.checkpoint4.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import br.com.fiap.checkpoint4.presentation.transferObjects.Event;

@Component
public class OrderConsumer {

    private final JmsTemplate queueJmsTemplate;

    public OrderConsumer(JmsTemplate queueJmsTemplate) {
        this.queueJmsTemplate = queueJmsTemplate;
    }

    // @JmsListener(destination = "order.receiver.queue", containerFactory =
    // "jmsListenerContainerFactory")
    // public void onQueueMessage(Event event) {
    // System.out.println("Consumer recebeu: " + event.payload());
    // if (event.payload().products().contains("pizza")) {
    // throw new RuntimeException("Sem estoque de Pizza");
    // }
    // }

    @JmsListener(destination = "order.receiver.queue", containerFactory = "jmsListenerContainerFactory")
    public void onQueueMessage(Event event) {
        try {
            System.out.println("QUEUE recebeu: " + event.payload());

            if (event.payload().products().contains("pizza")) {
                throw new RuntimeException("Sem estoque de Pizza");
            }
        } catch (RuntimeException error) {
            System.out.println("Erro encontrado: " + error.getMessage());
            Integer nextAttempt = event.attempt() + 1;

            if (event.attempt() >= 3) {
                queueJmsTemplate.convertAndSend("order.receiver.CustomDLQ", event);
                return;
            }

            Event newEvent = new Event(event.payload(), nextAttempt, error.getMessage());
            queueJmsTemplate.convertAndSend("order.receiver.queue", newEvent);
        }
    }

}
