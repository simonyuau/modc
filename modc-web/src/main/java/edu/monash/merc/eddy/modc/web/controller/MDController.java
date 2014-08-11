package edu.monash.merc.eddy.modc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by simonyu on 4/08/2014.
 */
@Controller
public class MDController {

    @RequestMapping("/home")
    public String home() {
        return "home";
    }
}
