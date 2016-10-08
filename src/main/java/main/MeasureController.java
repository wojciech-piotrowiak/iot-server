package main;

import java.net.URI;
import java.net.URISyntaxException;

import main.storage.PressureRepository;
import main.storage.TemperatureRepository;
import main.storage.pojo.Pressure;
import main.storage.pojo.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;



@RestController
public class MeasureController {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private PressureRepository  pressureRepository;

    @RequestMapping("/pressure")
    public String pressure(@RequestParam String value) {
        Pressure pressure=new Pressure();
        pressure.setDate(getCurrentDT());
        pressure.setValue(value);
        pressureRepository.save(pressure);
        return getCurrentDT();
    }

    @RequestMapping("/temperature")
    public String temperature(@RequestParam String value) {
        Temperature temperature=new Temperature();
        temperature.setDate(getCurrentDT());
        temperature.setValue(value);
        temperatureRepository.save(temperature);
        return getCurrentDT();
    }

    @RequestMapping("/allData")
    public String data()
    {
        String all ="";
      for(Temperature t:  temperatureRepository.findAll())
      {
          all+=t.getDate()+" tmp "+t.getValue();
      }

        for(Pressure p:pressureRepository.findAll())
        {all+=p.getDate()+" pressure "+p.getValue();}

        return all;
    }

    public String getCurrentDT()
    {
        RestTemplate restTemplate = new RestTemplate();
        try {
           return restTemplate.getForObject(new URI("http://www.timeapi.org/utc/now"),String.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

}
