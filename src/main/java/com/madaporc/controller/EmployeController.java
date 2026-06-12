package com.madaporc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.madaporc.model.Employe;
import com.madaporc.service.EmployeService;

@Controller
public class EmployeController {

    private final EmployeService employeService;

    public EmployeController(EmployeService employeService) {
        this.employeService = employeService;
    }

    @GetMapping("/employes")
    public String listEmployes(Model model) {

        List<Employe> employes = employeService.findAllEmployes();

        model.addAttribute("employes", employes);

        return "personnel/employes";
    }
}