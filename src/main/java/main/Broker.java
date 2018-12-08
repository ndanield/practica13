package main;

import org.apache.activemq.broker.BrokerService;
import org.h2.tools.Server;

import java.sql.SQLException;

public class Broker {
    public static void main(String[] args) {
        try {
            initDB();
            BrokerService brokerService = new BrokerService();
            brokerService.addConnector("tcp://0.0.0.0:61616");
            brokerService.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void initDB() {
        try {
            Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
