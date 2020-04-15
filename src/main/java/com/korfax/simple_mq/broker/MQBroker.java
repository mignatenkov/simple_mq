package com.korfax.simple_mq.broker;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.BrokerService;

@Slf4j
public class MQBroker extends Thread {

    private volatile boolean stopFlag;
    private final String addr;
    private final String brokerName;

    public MQBroker(String addr, String brokerName) {
        this.addr = addr;
        this.brokerName = brokerName;
    }

    public void run() {
        try {
            stopFlag = true;
            BrokerService broker = new BrokerService();
            broker.setUseJmx(true);
            broker.addConnector(addr);
            broker.setBrokerName(brokerName);
            broker.start();
            log.info("Broker " + brokerName + " started for " + addr);
            log.info("===============================");
            // now lets wait forever to avoid the JVM terminating immediately
            Object lock = new Object();
            while(stopFlag) {
                synchronized (lock) {
                    lock.wait(1000);
                }
            }
            broker.stop();
            log.info("Broker stopped");
        } catch (Exception e) {
            log.error("Error in broker", e);
        }
    }

    public void stopBroker() {
        stopFlag = false;
    }

}
