package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.madaporc.DTO.CycleProductionDTO;
import com.madaporc.service.CycleProductionService;

@Controller
public class CycleProductionController {
    private final CycleProductionService service;

    public CycleProductionController(CycleProductionService service) {
        this.service = service;
    }

    @GetMapping("/cycles")
    public String listCycles(Model model) {
        model.addAttribute("cycles", service.findAllCycles());
        return "production/cycles";
    }

    @GetMapping("/cycles/form")
    public String showCycleForm(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("cycle", new CycleProductionDTO());
        return "production/cycles";
    }

    @PostMapping("/cycles/save")
    public String saveCycle(@ModelAttribute CycleProductionDTO dto, Model model) {
        String e = dto.getId() == null ? service.creer(dto) : service.modifier(dto.getId(), dto);
        model.addAttribute("message", e);
        return "production/cycles";
    }
}
