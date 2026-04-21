package br.com.fiap.checkpoint4.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import br.com.fiap.checkpoint4.presentation.transferObjects.Event;

@Component
public class DLQConsumer {

    @JmsListener(destination = "ActiveMQ.DLQ", containerFactory = "jmsListenerContainerFactory")
    public void onDqlMessage(Event event) {
        System.out.println("\nDLQ automática recebeu: " + event.payload());
    }

    @JmsListener(destination = "order.receiver.dlq", containerFactory = "jmsListenerContainerFactory")
    public void onCustomDqlMessage(Event event) {
        System.out.println("\nDLQ manual recebeu: " + event.payload() + " Motivo do erro: " + event.error());
    }

}
