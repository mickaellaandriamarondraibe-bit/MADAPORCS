package com.madaporc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.madaporc.DTO.EmployeDTO;
import com.madaporc.model.Employe;
import com.madaporc.repository.PosteEmployeRepository;
import com.madaporc.repository.StatutEmployeRepository;
import com.madaporc.service.EmployeService;

@Controller
public class EmployeController {

    private final EmployeService employeService;
    private final PosteEmployeRepository posteEmployeRepository;
    private final StatutEmployeRepository statutEmployeRepository;

    public EmployeController(
            EmployeService employeService,
            PosteEmployeRepository posteEmployeRepository,
            StatutEmployeRepository statutEmployeRepository) {

        this.employeService = employeService;
        this.posteEmployeRepository = posteEmployeRepository;
        this.statutEmployeRepository = statutEmployeRepository;
    }

    @GetMapping("/employes")
    public String listEmployes(
            @RequestParam(required = false) String motCle,
            @RequestParam(required = false) Long posteId,
            @RequestParam(required = false) Long statutId,
            Model model) {

        List<Employe> employes = employeService.rechercherEmployes(motCle, posteId, statutId);

        model.addAttribute("employes", employes);
        model.addAttribute("motCle", motCle);
        model.addAttribute("postes", posteEmployeRepository.findAll());
        model.addAttribute("statuts", statutEmployeRepository.findAll());

        return "personnel/employes";
    }

    @GetMapping("/employes/form")
    public String showEmployeForm(
            @RequestParam(required = false) Long id,
            Model model) {

        prepareEmployeFormModel(model, id);

        return "personnel/formEmploye";
    }

    @PostMapping("/employes/save")
    public String saveEmploye(
            @ModelAttribute EmployeDTO dto,
            Model model) {

        if (dto.getId() == null) {
            employeService.creer(dto);
        } else {
            employeService.modifier(dto.getId(), dto);
        }

        return "redirect:/employes";
    }

    @PostMapping("/employes/archiver")
    public String archiverEmploye(
            @RequestParam Long id) {

        employeService.archiverEmploye(id);

        return "redirect:/employes";
    }

    private void prepareEmployeFormModel(
            Model model,
            Long id) {

        EmployeDTO dto = new EmployeDTO();

        if (id != null) {

            Employe employe = employeService.findEmployeById(id);

            dto.setId(employe.getId());
            dto.setNom(employe.getNom());
            dto.setPrenom(employe.getPrenom());
            dto.setContact(employe.getContact());
            dto.setAdresse(employe.getAdresse());
            dto.setDateEmbauche(employe.getDateEmbauche());
            dto.setSalaireBase(employe.getSalaireBase());

            if (employe.getPosteEmploye() != null) {
                dto.setPosteEmployeId(
                        employe.getPosteEmploye().getId());
            }

            if (employe.getStatutEmploye() != null) {
                dto.setStatutEmployeId(
                        employe.getStatutEmploye().getId());
            }
        }

        model.addAttribute("employe", dto);
        model.addAttribute(
                "postes",
                posteEmployeRepository.findAll());

        model.addAttribute(
                "statuts",
                statutEmployeRepository.findAll());
    }
}