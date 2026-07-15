package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.madaporc.model.DestinataireAlerte;
import com.madaporc.repository.DestinataireAlerteRepository;
import com.madaporc.service.EmailService;

@Controller
public class EmailController {

    private final DestinataireAlerteRepository destinataireRepository;
    private final EmailService emailService;

    public EmailController(DestinataireAlerteRepository destinataireRepository, EmailService emailService) {
        this.destinataireRepository = destinataireRepository;
        this.emailService = emailService;
    }

    @GetMapping("/email")
    public String liste(Model model) {
        model.addAttribute("destinataires", destinataireRepository.findAll());
        model.addAttribute("configure", emailService.isConfigured());
        return "email/destinataires";
    }

    @PostMapping("/email/ajouter")
    public String ajouter(@RequestParam String email, RedirectAttributes redirectAttributes) {
        String adresse = email == null ? "" : email.trim();
        if (adresse.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Veuillez saisir une adresse e-mail.");
        } else if (destinataireRepository.existsByEmail(adresse)) {
            redirectAttributes.addFlashAttribute("warning", "Cette adresse est déjà enregistrée.");
        } else {
            destinataireRepository.save(new DestinataireAlerte(adresse));
            redirectAttributes.addFlashAttribute("success", "Destinataire ajouté : " + adresse);
        }
        return "redirect:/email";
    }

    @PostMapping("/email/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        destinataireRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Destinataire supprimé.");
        return "redirect:/email";
    }
}
