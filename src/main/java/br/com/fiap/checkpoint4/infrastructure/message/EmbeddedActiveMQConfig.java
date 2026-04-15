package br.com.fiap.checkpoint4.infrastructure.message;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import jakarta.jms.ConnectionFactory;

@Configuration
public class EmbeddedActiveMQConfig {

    @Bean
    public BrokerService brokerService() throws Exception {
        BrokerService broker = new BrokerService();
        broker.setBrokerName("embedded-broker");
        broker.setPersistent(false);
        broker.setUseJmx(false);
        broker.addConnector("vm://embedded-broker");
        broker.start();
        return broker;
    }

    @Bean
    public ConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory amq = new ActiveMQConnectionFactory("vm://embedded-broker");
        return new CachingConnectionFactory(amq);
    }

    @Bean(name = "jmsListenerContainerFactory")
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MappingJackson2MessageConverter converter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        factory.setPubSubDomain(false);
        return factory;
    }

    @Bean(name = "topicListenerContainerFactory")
    public DefaultJmsListenerContainerFactory topicListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MappingJackson2MessageConverter converter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean
    public JmsTemplate queueJmsTemplate(
            ConnectionFactory connectionFactory,
            MappingJackson2MessageConverter converter) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(false);
        jmsTemplate.setMessageConverter(converter);
        return jmsTemplate;
    }

    @Bean
    public JmsTemplate topicJmsTemplate(
            ConnectionFactory connectionFactory,
            MappingJackson2MessageConverter converter) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.setMessageConverter(converter);
        return jmsTemplate;
    }

}