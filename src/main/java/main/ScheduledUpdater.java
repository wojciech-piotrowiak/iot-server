package main;

import main.storage.PresenceRepository;
import main.storage.PressureRepository;
import main.storage.TemperatureRepository;
import main.storage.pojo.Presence;
import main.storage.pojo.Pressure;
import main.storage.pojo.TempAndPressure;
import main.storage.pojo.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;


@RestController
public class ScheduledUpdater {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private PressureRepository pressureRepository;

    @Autowired
    private PresenceRepository presenceRepository;

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
            TempAndPressure tempAndPressure = restTemplate.getForObject(new URI("http://192.168.0.111/"), TempAndPressure.class);

            Pressure pressure = new Pressure();
            String currentDT = getCurrentDT();
            System.out.println("New measure with date: " + currentDT);
            pressure.setDate(currentDT);
            pressure.setValue(tempAndPressure.getPress());
            pressureRepository.save(pressure);

            Temperature temperature = new Temperature();
            temperature.setDate(currentDT);
            temperature.setValue(tempAndPressure.getTemp());
            temperatureRepository.save(temperature);
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