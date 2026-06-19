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
        return "cycles/list";
    }

    @GetMapping("/cycles/form")
    public String showCycleForm(@RequestParam(required = false) Long id, Model model) {
        CycleProductionDTO cycle = id == null ? new CycleProductionDTO() : service.findById(id);
        model.addAttribute("cycle", cycle);
        return "cycles/form";
    }

    @PostMapping("/cycles/save")
    public String saveCycle(@ModelAttribute CycleProductionDTO dto, Model model) {
        String e = dto.getId() == null ? service.creer(dto) : service.modifier(dto.getId(), dto);
        model.addAttribute("message", e);
        return "cycles/list";
    }
}
