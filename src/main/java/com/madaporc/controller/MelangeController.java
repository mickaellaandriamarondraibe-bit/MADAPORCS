package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.service.MelangeService;

@Controller
public class MelangeController {
    private final MelangeService service;

    public MelangeController(MelangeService service) {
        this.service = service;
    }

    @GetMapping("/melanges")
    public String index(Model model, HttpSession session) {
        return "melanges/list";
    }
}
