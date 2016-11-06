package main;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class Advice {

    @ModelAttribute("message")
    public String message() {
        return "['Wtorek', 3.2,6.9],\n" +
                "          ['Sroda', 1.1,2]";
    }

}
