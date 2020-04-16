package com.korfax.simple_mq.consumer;

import com.korfax.simple_mq.MainApp;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

@Slf4j
public class MQConsumer implements ExceptionListener {

    public void consume(String brokerAddr, int sessionType) {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerAddr);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            connection.setExceptionListener(this);

            // Create a Session
            Session session = connection.createSession(sessionType == Session.SESSION_TRANSACTED, sessionType);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(MainApp.QUEUE_NAME);

            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);

            // Wait for a message
            Message message = consumer.receive();

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                log.info("Received: " + text);
            } else {
                log.info("Received non text msg: " + message);
            }

            if (sessionType > Session.SESSION_TRANSACTED) {
                message.acknowledge();
            }

            consumer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            log.error("Caught: " + e);
        }
    }

    public void onException(JMSException e) {
        log.error("JMS Exception occured. Shutting down client.");
    }

}
