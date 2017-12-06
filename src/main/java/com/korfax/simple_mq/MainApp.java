package com.korfax.simple_mq;

import com.korfax.simple_mq.consumer.MQConsumer;
import com.korfax.simple_mq.producer.MQProducer;

/**
 * It works, assuming that MQ Broker is up on default settings, which are:
 * broker addr: tcp://localhost:61616
 * broker admin addr: http://localhost:8161 (default creds: admin/admin)
 */
public class MainApp {

    public static final String QUEUE_NAME = "TEST_QUEUE_1";
    public static final String ACTIVEMQ_ADDR = "tcp://localhost:61616";

    public static void main(String[] args) {
        System.out.println("This is a simple ActiveMQ test!");
        System.out.println("===============================");

        MQProducer mqProducer = new MQProducer();
        mqProducer.produce();

        MQConsumer mqConsumer = new MQConsumer();
        mqConsumer.consume();
    }

}
