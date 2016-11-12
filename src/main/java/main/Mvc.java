package main;

import main.storage.PressureRepository;
import main.storage.TemperatureRepository;
import main.storage.pojo.Pressure;
import main.storage.pojo.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Mvc {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private PressureRepository pressureRepository;


    @RequestMapping("/")
    public String listProducts(Model model){

        model.addAttribute("temperature", getTemp());
        model.addAttribute("pressure", getPressure());

        return "graphs";
    }

    public String getTemp() {
        List<String> values = new ArrayList<>();
        for (Temperature t : temperatureRepository.findAll()) {
            values.add(String.format("['%s', %s]", getDate(t.getDate()), t.getValue()));
        }

        return String.format("[%s]",values.stream().collect(Collectors.joining(",")));
    }

    public String getPressure() {
        List<String> values = new ArrayList<>();
        for (Pressure p : pressureRepository.findAll()) {
            values.add(String.format("['%s', %s]", getDate(p.getDate()), p.getValue()));
        }

        return String.format("[%s]",values.stream().collect(Collectors.joining(",")));
    }

    private String getDate(String t) {
        ZonedDateTime parse = ZonedDateTime.parse(t);
        return parse.getDayOfWeek() + " " + parse.getHour() + ":" + parse.getMinute();
    }


}
