package main;

import main.storage.PressureRepository;
import main.storage.TemperatureRepository;
import main.storage.pojo.Pressure;
import main.storage.pojo.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class Advice {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private PressureRepository pressureRepository;

    @ModelAttribute("temperature")
    public String getTemp() {
        List<String> values = new ArrayList<>();
        for (Temperature t : temperatureRepository.findAll()) {
            values.add(String.format("['%s', %s]", getDate(t.getDate()), t.getValue()));
        }

        return values.stream().collect(Collectors.joining(","));
    }

    @ModelAttribute("pressure")
    public String getPressure() {
        List<String> values = new ArrayList<>();
        for (Pressure p : pressureRepository.findAll()) {
            values.add(String.format("['%s', %s]", getDate(p.getDate()), p.getValue()));
        }

        return values.stream().collect(Collectors.joining(","));
    }

    private String getDate(String t) {
        ZonedDateTime parse = ZonedDateTime.parse(t).plusHours(2);
        return parse.getDayOfWeek() + " " + parse.getHour() + ":" + parse.getMinute();
    }

}
