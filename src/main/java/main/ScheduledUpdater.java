package main;

import main.storage.pojo.EspResponse;
import main.storage.service.DataService;
import main.storage.service.DefaultDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class ScheduledUpdater {

    private static final Logger logger = LoggerFactory.getLogger(DefaultDataService.class);
    @Autowired
    private DataService dataService;

    @Scheduled(cron = "0 */15 * * * *")
    public void getCurrentTempAndPressure() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            EspResponse espResponse = restTemplate.getForObject(new URI("http://192.168.0.111/"), EspResponse.class);
            dataService.saveMeasurements(espResponse);
        } catch (URISyntaxException e) {
            logger.error("Could not get current temp/pressure etc", e);
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void getPresence() {
        dataService.savePresence("dad", dataService.isDadAtHome());
        dataService.savePresence("mom", dataService.isMomAtHome());
    }
}