package main;

import freemarker.template.Configuration;
import freemarker.template.Version;
import org.eclipse.jetty.websocket.api.Session;
import services.BootStrapServices;
import services.Consumer;
import services.WebSocketHandler;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class Server {

    public static List<Session> sensorsConnected = new ArrayList<>();

    public static void main(String[] args) {

        Configuration configuration = new Configuration(new Version(2, 3, 23));
        configuration.setClassForTemplateLoading(Server.class, "/");
        staticFiles.location("/public");

        String topicName = "notificacion_sensores.topic";

        webSocket("/sensorsUpdate", WebSocketHandler.class);

        get("/", (req, res) -> {
//            StringWriter writer = new StringWriter();
//            Map<String, Object> atributos = new HashMap<>();
//            Template template = configuration.getTemplate("plantillas/index.ftl");
//
//            atributos.put("registros", "{\"registros\":" + JSON.toJson(ServicioEndpoint.getInstancia().listar()) + "}");
//
//            template.process(atributos, writer);

//            return writer;
            return "Hola mundo";
        });

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