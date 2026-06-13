package com.madaporc.controller;

import com.madaporc.DTO.MouvementStockDTO;
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
public class MouvementStockController {

    @GetMapping("/stocks/mouvements")
    public String listMouvementsStock(@RequestParam(required=false) Long ingredientId, @RequestParam(required=false) Long typeId, Model model) {
        model.addAttribute("titre", "Mouvements de Stock - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Mouvements de Stock - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "MouvementStockController");
        model.addAttribute("methodName", "listMouvementsStock");
        model.addAttribute("route", "/stocks/mouvements");
        return "placeholder";
    }

    

}
