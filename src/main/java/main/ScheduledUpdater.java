package main;

import main.storage.entities.Humidity;
import main.storage.entities.Presence;
import main.storage.entities.Pressure;
import main.storage.entities.Temperature;
import main.storage.pojo.EspResponse;
import main.storage.repositories.HumidityRepository;
import main.storage.repositories.PresenceRepository;
import main.storage.repositories.PressureRepository;
import main.storage.repositories.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

@Component
public class ScheduledUpdater {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private PressureRepository pressureRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private HumidityRepository humidityRepository;

    private String getCurrentDT() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(new URI("http://www.timeapi.org/cet/now"), String.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void getCurrentTempAndPressure() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            EspResponse espResponse = restTemplate.getForObject(new URI("http://192.168.0.111/"), EspResponse.class);

            Pressure pressure = new Pressure();
            String currentDT = getCurrentDT();
            System.out.println("New measure with date: " + currentDT);
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
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void getPresence() {
        try {
            String currentDT = getCurrentDT();
            InetAddress wojtek = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, 0, 103});
            InetAddress aga = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, 0, 105});

            Presence dadPresence = new Presence();
            dadPresence.setWho("dad");
            dadPresence.setPresent(wojtek.isReachable(500));
            dadPresence.setDate(currentDT);

            Presence momPresence = new Presence();
            momPresence.setWho("mom");
            momPresence.setPresent(aga.isReachable(500));
            momPresence.setDate(currentDT);

            presenceRepository.save(dadPresence);
            presenceRepository.save(momPresence);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}