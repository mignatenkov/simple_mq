package com.korfax.simple_mq;

import com.korfax.simple_mq.broker.MQBroker;
import com.korfax.simple_mq.consumer.MQConsumer;
import com.korfax.simple_mq.producer.MQProducer;
import lombok.extern.slf4j.Slf4j;

import javax.jms.DeliveryMode;
import javax.jms.Session;

@Slf4j
public class MainApp {

    public static final String QUEUE_NAME = "TEST_QUEUE_1";
    public static final String TCP_ACTIVEMQ_ADDR = "tcp://localhost:61617";
    public static final String VM_ACTIVEMQ_ADDR = "vm://vmBroker";

    public static void main(String[] args) throws Exception {
        log.info("This is a simple ActiveMQ test!");
        log.info("===============================");

        useBroker(TCP_ACTIVEMQ_ADDR, "tcpBroker");
        Thread.sleep(4000);
        useBroker(VM_ACTIVEMQ_ADDR, "vmBroker");
    }

    private static void useBroker(String addr, String brokerName) throws InterruptedException {
        log.info("===============================");
        log.info("Starting new broker!");

        MQBroker broker = new MQBroker(addr, brokerName);
        broker.start();
        Thread.sleep(3000);

        MQProducer mqProducer = new MQProducer();
        MQConsumer mqConsumer = new MQConsumer();
        String msg = "Hello from thread " + Thread.currentThread().getName();

        mqProducer.produce(addr, msg, Session.AUTO_ACKNOWLEDGE, DeliveryMode.NON_PERSISTENT);
        mqConsumer.consume(addr, Session.AUTO_ACKNOWLEDGE);
        log.info("-------------------------------");

        mqProducer.produce(addr, msg, Session.SESSION_TRANSACTED, DeliveryMode.NON_PERSISTENT);
        mqConsumer.consume(addr, Session.SESSION_TRANSACTED);
        log.info("-------------------------------");

        mqProducer.produce(addr, msg, Session.AUTO_ACKNOWLEDGE, DeliveryMode.PERSISTENT);
        mqConsumer.consume(addr, Session.AUTO_ACKNOWLEDGE);
        log.info("-------------------------------");

        mqProducer.produce(addr, msg, Session.SESSION_TRANSACTED, DeliveryMode.PERSISTENT);
        mqConsumer.consume(addr, Session.SESSION_TRANSACTED);
        log.info("===============================");

        broker.stopBroker();
    }

}
