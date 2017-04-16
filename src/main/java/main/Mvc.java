package main;

import main.storage.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Mvc {

    @Autowired
    private DataService dataService;

    @RequestMapping("/")
    public String listProducts(Model model) {

        model.addAttribute("temperature", dataService.getAllTemp());
        model.addAttribute("pressure", dataService.getAllPressure());
        model.addAttribute("humidity", dataService.getAllHumidity());
        model.addAttribute("momAtHome", dataService.isMomAtHome() ? "Mama w domu " : "");
        model.addAttribute("dadAtHome", dataService.isDadAtHome() ? "Tata w domu " : "");

        return "graphs";
    }
}
