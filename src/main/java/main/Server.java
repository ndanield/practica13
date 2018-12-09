package main;

import freemarker.template.Configuration;
import freemarker.template.Version;
import org.eclipse.jetty.websocket.api.Session;
import services.Consumer;
import services.WebSocketHandler;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class Server {

    public static List<Session> sensorsConnected = new ArrayList<>();

    public static void main(String[] args) {

        Configuration configuration = new Configuration(new Version(2, 3, 23));
        configuration.setClassForTemplateLoading(Server.class, "/templates");
        FreeMarkerEngine freemarkerEngine = new FreeMarkerEngine(configuration);
        staticFiles.location("/public");

        String topicName = "notificacion_sensores.topic";

        webSocket("/sensorsUpdate", WebSocketHandler.class);

        get("/", (req, res) -> {

            return new ModelAndView(null,"index.ftl");
        }, freemarkerEngine);

        Consumer consumer = new Consumer(topicName);
        try {
            consumer.connect();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public static void sendUpdateSensorsConnected(String message){
        for(Session session : sensorsConnected){
            try {
                session.getRemote().sendString(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}