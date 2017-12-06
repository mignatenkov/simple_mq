package com.korfax.simple_mq.consumer;

import com.korfax.simple_mq.MainApp;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MQConsumer implements ExceptionListener {

    public void consume() {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(MainApp.ACTIVEMQ_ADDR);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            connection.setExceptionListener(this);

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(MainApp.QUEUE_NAME);

            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);

            // Wait for a message
            Message message = consumer.receive(1000);

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("Received: " + text);
            } else {
                System.out.println("Received: " + message);
            }

            consumer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public void onException(JMSException e) {
        System.out.println("JMS Exception occured. Shutting down client.");
    }

}
