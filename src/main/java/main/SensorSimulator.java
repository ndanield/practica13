package main;

import models.SensorMessage;
import org.json.JSONObject;
import services.Producer;
import services.SensorMessageDAO;

import javax.jms.JMSException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SensorSimulator {
    public static void main(String[] args) throws JMSException, InterruptedException {
        String topicName = "notificacion_sensores.topic";
        System.out.println("Sensor #"+ args[0]);
        Producer producer = new Producer();

        for (int i = 0; i < 5; i++) {
            TimeUnit.SECONDS.sleep(1);

            String date = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss").format(new Date());
            int id = Integer.parseInt(args[0]);
            double temperature = Math.random() * 40;
            double humidicy = Math.random() * 100;

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("fechaGeneración", date);
            jsonObject.put("IdDispositivo", id);
            jsonObject.put("temperatura", temperature);
            jsonObject.put("humedad", humidicy);

//            Aqui se persiste el mensaje
//            SensorMessageDAO.getInstance().create(
//                    new SensorMessage(id,
//                                    date,
//                                    temperature,
//                                    humidicy));

            System.out.println(jsonObject.toString());
            producer.sendMessage(topicName, jsonObject.toString());
        }

        System.exit(0);
    }
}
