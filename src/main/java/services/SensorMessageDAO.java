package services;

import models.SensorMessage;

public class SensorMessageDAO extends DAO<SensorMessage> {
    private static SensorMessageDAO instance;

    private SensorMessageDAO() {
        super(SensorMessage.class);
    }

    public static SensorMessageDAO getInstance() {
        if (instance == null) {
            instance = new SensorMessageDAO();
        }
        return instance;
    }
}
