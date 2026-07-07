package com.madaporc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.madaporc.dto.EvenementCalendrier;
import com.madaporc.service.CalendrierService;

@Controller
public class CalendrierController {

    private final CalendrierService calendrierService;

    public CalendrierController(CalendrierService calendrierService) {
        this.calendrierService = calendrierService;
    }

    @GetMapping("/calendrier")
    public String page() {
        return "calendrier/index";
    }

    @GetMapping("/calendrier/events")
    @ResponseBody
    public List<EvenementCalendrier> events() {
        return calendrierService.tousLesEvenements();
    }
}
