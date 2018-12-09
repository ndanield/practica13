package services;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Producer {

    public Producer(){

    }

    /**
     *
     * @param topicName el id del topico
     * @param message el mensaje a pasar
     * @throws JMSException Si hay un problema de JMS
     */
    public void sendMessage(String topicName, String message) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = factory.createConnection("admin", "admin");
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(topicName);
        MessageProducer producer = session.createProducer(topic);

        // Esto es lo que se enviara
        TextMessage textMessage = session.createTextMessage(message);
        producer.send(textMessage);

        producer.close();
        session.close();
        connection.stop();
    }
}
