package br.com.fiap.checkpoint4.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import br.com.fiap.checkpoint4.presentation.transferObjects.Event;

@Component
public class OrderConsumer {

    private static final int ATTEMPTS_LIMIT = 3;

    private final JmsTemplate queueJmsTemplate;

    public OrderConsumer(JmsTemplate queueJmsTemplate) {
        this.queueJmsTemplate = queueJmsTemplate;
    }

    /*
     * Método sem tratamento de exceção (catch) propagando-a até o container JMS
     * Mensagens com falha de processamento serão encaminhadas para fila
     * ActiveMQ.DLQ automaticamente após exceder o limite de redelivery
     */
    @JmsListener(destination = "order.receiver.queue", containerFactory = "jmsListenerContainerFactory")
    // public void onQueueMessage(Event event) {
    // System.out.println("\nConsumer: pedido recebido " + event.payload());

    // if (event.payload().products().contains("pizza")) {
    // System.out.println("ERROR: Forno quebrou");
    // throw new RuntimeException("Forno quebrou");
    // }

    // System.out.println("Consumer: Pedido processado com sucesso");
    // }

    /*
     * Método com tratamento de exceção (catch)
     * Mensagens com falha devem ser tratadas manualmente e encaminhadas para fila
     * order.receiver.dlq após exceder o limite de redelivery configurado
     */
    @JmsListener(destination = "order.receiver.queue", containerFactory = "jmsListenerContainerFactory")
    public void onQueueMessage(Event event) {
        try {
            System.out.println("\nConsumer: Pedido recebido " + event.payload());

            if (event.payload().products().contains("pizza")) {
                System.out.println("ERROR: Forno quebrou");
                throw new RuntimeException("Forno quebrou");
            }

            System.out.println("Consumer: Pedido processado com sucesso");
        } catch (RuntimeException error) {
            Integer nextAttempt = event.attempt() + 1;

            System.out.println("Tratamento de Error: " + error.getMessage() + " Tentativa " + nextAttempt);

            if (event.attempt() >= ATTEMPTS_LIMIT) {
                System.out.println("Limite de tentativas excedido. Encaminhando para DLQ");
                queueJmsTemplate.convertAndSend("order.receiver.dlq",
                        new Event(event.payload(), event.attempt(), error.getMessage()));
                return;
            }

            queueJmsTemplate.convertAndSend("order.receiver.queue",
                    new Event(event.payload(), nextAttempt, error.getMessage()));
            System.out.println("Pedido enviado para queue novamente.");
        }
    }

}
