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

        for (int dm = 1; dm <= 2; dm++) {
            // NON_PERSISTENT = 1
            // PERSISTENT = 2
            for (int st = 0; st <= 3; st++) {
                // SESSION_TRANSACTED = 0
                // AUTO_ACKNOWLEDGE = 1
                // CLIENT_ACKNOWLEDGE = 2
                // DUPS_OK_ACKNOWLEDGE = 3
                log.info("SessionType = "+st+", DeliveryMode = "+dm);
                long start = System.currentTimeMillis();
                mqProducer.produce(addr, msg, st, dm);
                mqConsumer.consume(addr, st);
//                mqConsumer.consume(addr, st);
                log.info("Time: " + (System.currentTimeMillis() - start) + "ms");
                log.info("-------------------------------");
            }
        }

        broker.stopBroker();
    }

}
