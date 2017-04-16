package main.storage.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.storage.entities.*;
import main.storage.pojo.EspResponse;
import main.storage.repositories.HumidityRepository;
import main.storage.repositories.PresenceRepository;
import main.storage.repositories.PressureRepository;
import main.storage.repositories.TemperatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class DefaultDataService implements DataService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultDataService.class);
    @Autowired
    private TemperatureRepository temperatureRepository;
    @Autowired
    private PressureRepository pressureRepository;
    @Autowired
    private PresenceRepository presenceRepository;
    @Autowired
    private HumidityRepository humidityRepository;

    @Override
    public Boolean isMomAtHome() {
        try {
            InetAddress aga = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, 0, 105});
            return aga.isReachable(500);
        } catch (IOException e) {
            logger.error("Could not check presence of mom", e);
        }
        return false;
    }

    @Override
    public Boolean isDadAtHome() {
        try {
            InetAddress wojtek = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, 0, 103});
            return wojtek.isReachable(500);
        } catch (IOException e) {
            logger.error("Could not check presence of dad", e);
        }
        return false;
    }

    @Override
    public void savePresence(String who, Boolean reachable) {
        Presence presence = new Presence();
        presence.setWho(who);
        presence.setPresent(reachable);
        presence.setDate(getCurrentDT());
        presenceRepository.save(presence);
    }

    @Override
    public void saveMeasurements(EspResponse espResponse) {
        Pressure pressure = new Pressure();
        String currentDT = getCurrentDT();
        logger.info("New measure with date: " + currentDT);
        pressure.setDate(currentDT);
        pressure.setValue(espResponse.getPress());
        pressureRepository.save(pressure);

        Temperature temperature = new Temperature();
        temperature.setDate(currentDT);
        temperature.setValue(espResponse.getTemp());
        temperatureRepository.save(temperature);

        if (!espResponse.getHum().contentEquals("nan")) {
            Humidity humidity = new Humidity();
            humidity.setDate(currentDT);
            humidity.setValue(espResponse.getHum());
            humidityRepository.save(humidity);
        }
    }

    @Override
    public String getAllTemp() {
        return temperatureRepository.streamAll().map(this::getEntry)
                .collect(getJoining());
    }


    @Override
    public String getAllPressure() {
        return pressureRepository.streamAll().map(this::getEntry)
                .collect(getJoining());
    }

    @Override
    public String getAllHumidity() {
        return humidityRepository.streamAll().map(this::getEntry)
                .collect(getJoining());
    }

    private Collector<CharSequence, ?, String> getJoining() {
        return Collectors.joining(",", "[", "]");
    }

    private String getEntry(ValueRecord t) {
        return String.format("['%s', %s]", parseOutputDate(t.getDate()), t.getValue());
    }

    private String parseOutputDate(String t) {
        DateTimeFormatter rfc_1123_date_time = DateTimeFormatter.RFC_1123_DATE_TIME;
        ZonedDateTime parse = ZonedDateTime.parse(t, rfc_1123_date_time);
        return String.format("%s %s:%s", parse.getDayOfWeek(), parse.getHour(), parse.getMinute());
    }

    private String getCurrentDT() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return parseAPIDate(restTemplate.getForObject(new URI("https://script.google.com/macros/s/AKfycbyd5AcbAnWi2Yn0xhFRbyzS4qMq1VucMVgVvhul5XqS9HkAyJY/exec?tz=Europe/Warsaw"), String.class));
        } catch (IOException | URISyntaxException e) {
            logger.error("Could not get current dateTime", e);
        }
        return "";
    }

    private String parseAPIDate(String input) throws IOException {
        JsonFactory factory = new JsonFactory();

        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode = mapper.readTree(input);

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            if ("fulldate".contentEquals(field.getKey())) {
                return field.getValue().textValue();
            }
        }
        return "";
    }
}
