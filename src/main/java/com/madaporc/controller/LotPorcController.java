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

@Controller
public class LotPorcController {

    @Autowired
    private LotPorcService lotPorcService;

    @GetMapping("/lots")
    public String listLots(@RequestParam(value = "code", required = false) String code,
                           @RequestParam(value = "raceId", required = false) Long raceId,
                           @RequestParam(value = "statutId", required = false) Long statutId,
                           Model model) {
        lotPorcService.prepareLotListModel(model, code, raceId, statutId);
        model.addAttribute("titre", "Liste des Lots");
        model.addAttribute("pageActive", "lots");
        return "lots/listeLots";
    }

    @GetMapping("/lots/form")
    public String showForm(@RequestParam(required = false) Long id,
                           @RequestParam(required = false) String typeEntree,
                           Model model) {
        lotPorcService.prepareLotFormModel(model, id, typeEntree);
        model.addAttribute("titre", id != null ? "Modifier un Lot" : "Ajouter un Lot");
        model.addAttribute("pageActive", "lots");
        return "lots/formLot";
    }

    @PostMapping("/lots/save")
    public String saveLot(@ModelAttribute LotPorcDTO dto,
                          Model model,
                          HttpSession session) {
        Long utilisateurId = (Long) session.getAttribute("utilisateurId");

        if (utilisateurId == null) {
            model.addAttribute("erreur", "Utilisateur non authentifié");
            return "lots/formLot";
        }

        String resultat;
        if (dto.getId() != null) {
            resultat = lotPorcService.modifier(dto.getId(), dto);
        } else {
            resultat = lotPorcService.creer(dto, utilisateurId);
        }

        if (resultat.contains("succès")) {
            model.addAttribute("succes", resultat);
            return "redirect:/lots";
        } else {
            model.addAttribute("erreur", resultat);
            return "lots/formLot";
        }
    }

    @GetMapping("/lots/{id}")
    public String detailLot(@PathVariable Long id, Model model) {
        LotDetailDTO detail = lotPorcService.getDetailLot(id);

        if (detail == null) {
            model.addAttribute("erreur", "Lot non trouvé");
            return "redirect:/lots";
        }

        model.addAttribute("lotDetail", detail);
        model.addAttribute("titre", "Détail du Lot");
        model.addAttribute("pageActive", "lots");
        model.addAttribute("tabActive", "general");
        return "lots/detailLot";
    }

    @GetMapping("/lots/{id}/tab/{tab}")
    public String detailLotTab(@PathVariable Long id,
                               @PathVariable String tab,
                               Model model) {
        LotDetailDTO detail = lotPorcService.getDetailLot(id);

        if (detail == null) {
            model.addAttribute("erreur", "Lot non trouvé");
            return "redirect:/lots";
        }

        model.addAttribute("lotDetail", detail);
        model.addAttribute("titre", "Détail du Lot");
        model.addAttribute("pageActive", "lots");
        model.addAttribute("tabActive", tab);
        return "lots/detailLot";
    }

    @PostMapping("/lots/archive/{id}")
    public String archiver(@PathVariable Long id, Model model) {
        String resultat = lotPorcService.archiverLot(id);
        model.addAttribute("succes", resultat);
        return "redirect:/lots";
    }
}