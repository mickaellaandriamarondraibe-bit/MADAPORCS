package com.madaporc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.madaporc.model.Employe;
import com.madaporc.repository.PosteEmployeRepository;
import com.madaporc.repository.StatutEmployeRepository;
import com.madaporc.service.EmployeService;

@Controller
public class EmployeController {

    private final EmployeService employeService;
    private final PosteEmployeRepository posteEmployeRepository;
    private final StatutEmployeRepository statutEmployeRepository;

    public EmployeController(EmployeService employeService, PosteEmployeRepository posteEmployeRepository, StatutEmployeRepository statutEmployeRepository) {
        this.employeService = employeService;
        this.posteEmployeRepository = posteEmployeRepository;
        this.statutEmployeRepository = statutEmployeRepository;
    }

    @GetMapping("/employes")
    public String listEmployes(Model model) {

        List<Employe> employes = employeService.findAllEmployes();

        model.addAttribute("employes", employes);

        return "personnel/employes";
    }

    @GetMapping("/employes/new")
    public String newEmploye(Model model) {
        model.addAttribute("employe", new Employe());
        model.addAttribute("postes", posteEmployeRepository.findAll());
        model.addAttribute("statuts", statutEmployeRepository.findAll());

        return "personnel/formEmploye";
    }

    @GetMapping("/employes/edit/{id}")
    public String editEmploye(@PathVariable Long id, Model model) {
        model.addAttribute("employe", employeService.findEmployeById(id));
        model.addAttribute("postes", posteEmployeRepository.findAll());
        model.addAttribute("statuts", statutEmployeRepository.findAll());

        return "personnel/formEmploye";
    }

    @PostMapping("/employes/save")
    public String saveEmploye(@ModelAttribute Employe employe, @RequestParam(required = false) Long posteEmployeId, @RequestParam(required = false) Long statutEmployeId) {
        employeService.saveEmploye(employe, posteEmployeId, statutEmployeId);

        return "redirect:/employes";
    }

    @PostMapping("/employes/delete/{id}")
    public String deleteEmploye(@PathVariable Long id) {
        employeService.deleteEmploye(id);

        return "redirect:/employes";
    }

    @PostMapping("/employes/desactiver/{id}")
    public String desactiverEmploye(@PathVariable Long id) {
        employeService.desactiverEmploye(id);

        return "redirect:/employes";
    }
}
