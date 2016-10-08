package hello;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MeasureController {

    @RequestMapping("/pressure")
    public String pressure(@RequestParam String value) {
        return getCurrentDT();
    }

    @RequestMapping("/temperature")
    public String temperature(@RequestParam String value) {
        return getCurrentDT();
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
