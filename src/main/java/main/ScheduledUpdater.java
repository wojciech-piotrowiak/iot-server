package main;

import main.storage.pojo.EspResponse;
import main.storage.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class ScheduledUpdater {

    @Autowired
    private DataService dataService;


    @Scheduled(cron = "0 */15 * * * *")
    public void getCurrentTempAndPressure() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            EspResponse espResponse = restTemplate.getForObject(new URI("http://192.168.0.111/"), EspResponse.class);
            dataService.saveMeasurements(espResponse);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void getPresence() {
        dataService.savePresence("dad", dataService.isDadAtHome());
        dataService.savePresence("mom", dataService.isMomAtHome());
    }
}