package main.storage.service;

import main.storage.pojo.EspResponse;

public interface DataService {
    Boolean isMomAtHome();

    Boolean isDadAtHome();

    void savePresence(String who, Boolean reachable);

    void saveMeasurements(EspResponse espResponse);

    String getAllTemp();

    String getAllPressure();

    String getAllHumidity();
}
