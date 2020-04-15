package com.korfax.simple_mq.producer;

import com.korfax.simple_mq.MainApp;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

@Slf4j
public class MQProducer {

    public void produce(String brokerAddr, String msg, int sessionType, int deliveryMode) {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerAddr);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(sessionType == Session.SESSION_TRANSACTED, sessionType);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(MainApp.QUEUE_NAME);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(deliveryMode);

            // Create a messages
            TextMessage message = session.createTextMessage(msg);

            // Tell the producer to send the message
            producer.send(message);

            if (sessionType == Session.SESSION_TRANSACTED) {
                session.commit();
            }

            log.info("Sent message: "+ msg);

            // Clean up
            session.close();
            connection.close();
        } catch (Exception e) {
            log.error("Caught: " + e);
        }
    }

}
