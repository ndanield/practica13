package services;

import main.Server;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer {

    ActiveMQConnectionFactory factory;
    Connection connection;
    Session session;
    Topic topic;
    MessageConsumer consumer;
    String topicName;


    /**
     *
     */
    public Consumer(String topicName) {
        this.topicName = topicName;
    }

    /**
     *
     * @throws JMSException
     */
    public void connect() throws JMSException {
        factory = new ActiveMQConnectionFactory("admin", "admin", "failover:tcp://localhost:61616");
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        topic = session.createTopic(topicName);
        consumer = session.createConsumer(topic);

        consumer.setMessageListener(message -> {
            try {
                TextMessage messageText = (TextMessage) message;
                Server.sendUpdateSensorsConnected(messageText.getText());
            }catch(Exception ex){
                ex.printStackTrace();
            }
        });
    }

    public void disconnect() throws JMSException {
        connection.stop();
        connection.close();
    }
}
