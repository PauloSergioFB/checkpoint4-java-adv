package br.com.fiap.checkpoint4.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import br.com.fiap.checkpoint4.presentation.transferObjects.OrderDTO;

@Component
public class OrderConsumer {

    @JmsListener(destination = "order.receiver.queue", containerFactory = "jmsListenerContainerFactory")
    public void onQueueMessage(OrderDTO orderDTO) {
        System.out.println("QUEUE recebeu: " + orderDTO);
    }

}
