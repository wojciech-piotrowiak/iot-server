package main;

import main.storage.PressureRepository;
import main.storage.TemperatureRepository;
import main.storage.pojo.Pressure;
import main.storage.pojo.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    public String listProducts(Model model) {

        model.addAttribute("temperature", getTemp());
        model.addAttribute("pressure", getPressure());
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

    private String isMomAtHome() {
        try {
            InetAddress aga = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, 0, 105});
            return aga.isReachable(500) ? "Mama w domu ":"";
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
            return wojtek.isReachable(500) ? "Tata w domu":"";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Błąd 103";
    }

    private String parseDate(String t) {
        ZonedDateTime parse = ZonedDateTime.parse(t);
        return parse.getDayOfWeek() + " " + parse.getHour() + ":" + parse.getMinute();
    }
}
