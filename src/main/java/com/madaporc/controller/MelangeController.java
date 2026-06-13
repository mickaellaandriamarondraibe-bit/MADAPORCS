package com.madaporc.controller;

import com.madaporc.DTO.MelangeDTO;
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
public class MelangeController {

    @GetMapping("/melanges")
    public String listMelanges(Model model) {
        model.addAttribute("titre", "Melanges Alimentaires / Feed Formulations - MADAPORC");
        model.addAttribute("referenceFigma", "Melanges Alimentaires / Feed Formulations - MADAPORC");
        model.addAttribute("controllerName", "MelangeController");
        model.addAttribute("methodName", "listMelanges");
        model.addAttribute("route", "/melanges");
        return "placeholder";
    }


}
