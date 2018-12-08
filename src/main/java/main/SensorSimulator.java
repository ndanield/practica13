package main;

import services.Producer;

import javax.jms.JMSException;

public class SensorSimulator {
    public static void main(String[] args) throws JMSException {
        String topicName = "notificacion_sensores.topic";
        System.out.println("Sensor #"+ args[0]);
        new Producer().sendMessage(topicName, "Hola mundo");
    }
}
