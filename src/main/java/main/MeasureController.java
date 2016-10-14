package main;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import main.storage.PressureRepository;
import main.storage.TemperatureRepository;
import main.storage.pojo.Pressure;
import main.storage.pojo.TempAndPressure;
import main.storage.pojo.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;


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

    @RequestMapping("/tempGraph")
    void getGraph(HttpServletResponse response) throws IOException {
        String c="|";
        List<Temperature> temperatureList=new ArrayList<>();
        for (Temperature t : temperatureRepository.findAll()) {
            temperatureList.add(t);
            ZonedDateTime parse = ZonedDateTime.parse(t.getDate()).plusHours(2);
            c+=parse.getHour()+":"+parse.getMinute()+"|";
        }
        String values = temperatureList.stream().map(t -> t.getValue()).collect(Collectors.joining(","));
        String minMax = getMinMax(temperatureList);

        String scale=getScale(temperatureList);

        //http://chart.apis.google.com/chart?chs=500x300&cht=lc&chd=t:26.60,26.60,26.30,26.60,26.60
        // &chds=26.30,26.60&chco=FF0000&chls=6&chxt=x,y&chxl=0:|0|1|2|3|4|1:||15|20|30&chf=bg,s,efefef
        response.sendRedirect("http://chart.apis.google.com/chart?chs=600x200" +
                "&cht=lc&chd=t:" +values+minMax+
                "&chco=FF0000&chls=6&chxt=x,y&chxl=0:"+c+"1:||"+scale+"&chf=bg,s,efefef");
    }

    private String getMinMax(List<Temperature> temperatureList) {
        String minMax="&chds=";
        DoubleSummaryStatistics stat = temperatureList.stream().mapToDouble(t -> Double.valueOf(t.getValue())).summaryStatistics();
        minMax+=stat.getMin()+","+stat.getMax();
        return minMax;
    }

    private String getScale(List<Temperature> temperatureList) {
        DoubleSummaryStatistics stat = temperatureList.stream().mapToDouble(t -> Double.valueOf(t.getValue())).summaryStatistics();
        String scale=(stat.getMin()-2)+"|"+(stat.getMax()+2);
        return scale;
    }

    @Scheduled(cron = "0 */1 * * * *")
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
