package com.madaporc.controller;

import com.madaporc.DTO.LotDetailDTO;
import com.madaporc.DTO.LotPorcDTO;
import com.madaporc.service.LotPorcService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LotPorcController {

    @Autowired
    private LotPorcService lotPorcService;

    @GetMapping("/lots")
    public String listLots(@RequestParam(required = false) String code,
                           @RequestParam(required = false) Long raceId,
                           @RequestParam(required = false) Long statutId,
                           Model model) {
        lotPorcService.prepareLotListModel(model, code, raceId, statutId);
        model.addAttribute("titre", "Liste des Lots");
        model.addAttribute("pageActive", "lots");
        return "lots/listeLots";
    }

    @GetMapping("/lots/form")
    public String afficherFormulaire(@RequestParam(required = false) Long id,
                                     @RequestParam(required = false) String typeEntree,
                                     Model model) {
        lotPorcService.prepareLotFormModel(model, id, typeEntree);
        model.addAttribute("titre", id != null ? "Modifier un Lot" : "Ajouter un Lot");
        model.addAttribute("pageActive", "lots");
        return "lots/formLot";
    }

    @PostMapping("/lots/save")
    public String sauvegarderLot(@ModelAttribute LotPorcDTO lotPorcDTO,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Long utilisateurId = (Long) session.getAttribute("utilisateurId");

        if (utilisateurId == null) {
            redirectAttributes.addFlashAttribute("erreur", "Utilisateur non authentifié");
            return "redirect:/lots";
        }

        String resultat;
        if (lotPorcDTO.getId() != null) {
            resultat = lotPorcService.modifier(lotPorcDTO.getId(), lotPorcDTO);
        } else {
            resultat = lotPorcService.creer(lotPorcDTO, utilisateurId);
        }

        if (resultat.contains("succès")) {
            redirectAttributes.addFlashAttribute("succes", resultat);
            return "redirect:/lots";
        } else {
            redirectAttributes.addFlashAttribute("erreur", resultat);
            if (lotPorcDTO.getId() != null) {
                return "redirect:/lots/form?id=" + lotPorcDTO.getId();
            } else {
                return "redirect:/lots/form";
            }
        }
    }

    @GetMapping("/lots/{id}")
    public String afficherDetailLot(@PathVariable Long id, Model model) {
        LotDetailDTO detail = lotPorcService.getDetailLot(id);

        if (detail == null) {
            model.addAttribute("erreur", "Lot non trouvé");
            return "redirect:/lots";
        }

        model.addAttribute("lotDetail", detail);
        model.addAttribute("titre", "Détail du Lot");
        model.addAttribute("pageActive", "lots");
        return "lots/detailLot";
    }
}