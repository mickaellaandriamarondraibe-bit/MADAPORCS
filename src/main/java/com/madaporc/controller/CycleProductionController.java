package com.madaporc.controller;

import com.madaporc.DTO.CycleProductionDTO;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CycleProductionController {

    @GetMapping("/cycles")
    public String listCycles(Model model) {
        model.addAttribute("titre", "Cycles de Production - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Cycles de Production - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "CycleProductionController");
        model.addAttribute("methodName", "listCycles");
        model.addAttribute("route", "/cycles");
        return "placeholder";
    }


}
