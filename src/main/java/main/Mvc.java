package main;

import main.storage.entities.Humidity;
import main.storage.entities.Pressure;
import main.storage.entities.Temperature;
import main.storage.repositories.HumidityRepository;
import main.storage.repositories.PressureRepository;
import main.storage.repositories.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Mvc {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private PressureRepository pressureRepository;

    @Autowired
    private HumidityRepository humidityRepository;


    @RequestMapping("/")
    public String listProducts(Model model) {

        model.addAttribute("temperature", getTemp());
        model.addAttribute("pressure", getPressure());
        model.addAttribute("humidity", getHumidity());
        model.addAttribute("momAtHome", isMomAtHome());
        model.addAttribute("dadAtHome", isDadAtHome());

        return "graphs";
    }

    public String getTemp() {
        List<String> values = new ArrayList<>();
        for (Temperature t : temperatureRepository.findAll()) {
            values.add(String.format("['%s', %s]", parseDate(t.getDate()), t.getValue()));
        }

        return String.format("[%s]", values.stream().collect(Collectors.joining(",")));
    }

    public String getPressure() {
        List<String> values = new ArrayList<>();
        for (Pressure p : pressureRepository.findAll()) {
            values.add(String.format("['%s', %s]", parseDate(p.getDate()), p.getValue()));
        }

        return String.format("[%s]", values.stream().collect(Collectors.joining(",")));
    }

    public String getHumidity() {
        List<String> values = new ArrayList<>();
        for (Humidity p : humidityRepository.findAll()) {
            values.add(String.format("['%s', %s]", parseDate(p.getDate()), p.getValue()));
        }

        return String.format("[%s]", values.stream().collect(Collectors.joining(",")));
    }

    private String isMomAtHome() {
        try {
            InetAddress aga = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, 0, 105});
            return aga.isReachable(500) ? "Mama w domu " : "";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Błąd 105";
    }

    private String isDadAtHome() {
        try {
            InetAddress wojtek = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, 0, 103});
            return wojtek.isReachable(500) ? "Tata w domu" : "";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Błąd 103";
    }

    private String parseDate(String t) {
        DateTimeFormatter rfc_1123_date_time = DateTimeFormatter.RFC_1123_DATE_TIME;
        ZonedDateTime parse = ZonedDateTime.parse(t, rfc_1123_date_time);
        return String.format("%s %s:%s", parse.getDayOfWeek(), parse.getHour(), parse.getMinute());
    }
}
