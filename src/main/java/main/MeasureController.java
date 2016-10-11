package main;

import java.net.URI;
import java.net.URISyntaxException;

import main.storage.PressureRepository;
import main.storage.TemperatureRepository;
import main.storage.pojo.Pressure;
import main.storage.pojo.TempAndPressure;
import main.storage.pojo.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class MeasureController {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private PressureRepository pressureRepository;

    @RequestMapping("/allData")
    public String data() {
        String all = "";
        for (Temperature t : temperatureRepository.findAll()) {
            all += t.getDate() + " tmp " + t.getValue();
        }

        for (Pressure p : pressureRepository.findAll()) {
            all += p.getDate() + " pressure " + p.getValue();
        }

        return all;
    }

    public String getCurrentDT() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(new URI("http://www.timeapi.org/utc/now"), String.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Scheduled(cron = "0 */10 * * * *")
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

}
