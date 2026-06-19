package com.madaporc.controller;

import java.time.LocalDate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.DTO.MouvementLotDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.TypeMouvementLot;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.MouvementLotPorcRepository;
import com.madaporc.repository.TypeMouvementLotRepository;
import com.madaporc.service.MouvementLotService;

@Controller
public class MouvementLotController {
    private final MouvementLotService service;
    private final MouvementLotPorcRepository mouvementRepository;
    private final LotPorcRepository lotRepository;
    private final TypeMouvementLotRepository typeMouvementLotRepository;

    public MouvementLotController(
            MouvementLotService service,
            MouvementLotPorcRepository mouvementRepository,
            LotPorcRepository lotRepository,
            TypeMouvementLotRepository typeMouvementLotRepository
    ) {
        this.service = service;
        this.mouvementRepository = mouvementRepository;
        this.lotRepository = lotRepository;
        this.typeMouvementLotRepository = typeMouvementLotRepository;
    }

    @GetMapping("/lots/mouvements")
    public String listAllMouvements(
            @RequestParam(required = false) String motCle,
            @RequestParam(required = false) Long typeMouvementId,
            @RequestParam(required = false) String date,
            Model model
    ) {
        LocalDate filterDate = date == null || date.isBlank() ? null : LocalDate.parse(date);
        model.addAttribute("mouvements", service.rechercherMouvements(motCle, typeMouvementId, filterDate, null));
        model.addAttribute("motCle", motCle);
        model.addAttribute("typeMouvementId", typeMouvementId);
        model.addAttribute("date", date);
        model.addAttribute("typesMouvement", typeMouvementLotRepository.findAll());
        model.addAttribute("lotLibelles", lotRepository.findAll().stream()
                .collect(Collectors.toMap(LotPorc::getId, LotPorc::getCodeLot)));
        model.addAttribute("typeLibelles", typeMouvementLotRepository.findAll().stream()
                .collect(Collectors.toMap(TypeMouvementLot::getId, TypeMouvementLot::getLibelle)));
        model.addAttribute("totalMouvements", mouvementRepository.findAllByOrderByDateMouvementDesc().size());
        return "lots/mouvements/list";
    }

    @GetMapping("/lots/{id}/mouvements")
    public String listMouvements(@PathVariable Long id,
                                 @RequestParam(required = false) String motCle,
                                 @RequestParam(required = false) Long typeMouvementId,
                                 @RequestParam(required = false) String date,
                                 Model model) {
        LocalDate filterDate = date == null || date.isBlank() ? null : LocalDate.parse(date);
        model.addAttribute("mouvements", service.rechercherMouvements(motCle, typeMouvementId, filterDate, id));
        model.addAttribute("lotId", id);
        model.addAttribute("motCle", motCle);
        model.addAttribute("typeMouvementId", typeMouvementId);
        model.addAttribute("date", date);
        model.addAttribute("typesMouvement", typeMouvementLotRepository.findAll());
        model.addAttribute("lotLibelles", lotRepository.findAll().stream()
                .collect(Collectors.toMap(LotPorc::getId, LotPorc::getCodeLot)));
        model.addAttribute("typeLibelles", typeMouvementLotRepository.findAll().stream()
                .collect(Collectors.toMap(TypeMouvementLot::getId, TypeMouvementLot::getLibelle)));
        model.addAttribute("totalMouvements", service.rechercherMouvements(null, null, null, id).size());
        return "lots/mouvements/list";
    }

    @GetMapping("/lots/mouvements/form")
    public String showMouvementForm(@RequestParam(required = false) Long id, Model model) {
        MouvementLotDTO dto = new MouvementLotDTO();

        if (id != null) {
            mouvementRepository.findById(id).ifPresent(mouvement -> {
                dto.setId(mouvement.getId());
                dto.setLotPorcId(mouvement.getLotPorcId());
                dto.setTypeMouvementLotId(mouvement.getTypeMouvementLotId());
                dto.setQuantite(mouvement.getQuantite());
                dto.setDateMouvement(mouvement.getDateMouvement());
                dto.setMotif(mouvement.getMotif());
            });
        }

        prepareFormModel(model, dto);
        return "lots/mouvements/form";
    }

    @GetMapping("/lots/mouvements/detail/{id}")
    public String detailMouvement(@PathVariable long id, Model model) {
        mouvementRepository.findById(id).ifPresent(mouvement -> {
            model.addAttribute("mouvement", mouvement);
            Long lotPorcId = mouvement.getLotPorcId();
            Long typeMouvementLotId = mouvement.getTypeMouvementLotId();
            model.addAttribute("lot", lotPorcId != null ? lotRepository.findById(lotPorcId).orElse(null) : null);
            model.addAttribute("typeMouvement", typeMouvementLotId != null ? typeMouvementLotRepository.findById(typeMouvementLotId).orElse(null) : null);
        });
        return "lots/mouvements/detail";
    }

    @GetMapping("/lots/mouvements/{id}")
    public String detailMouvementAlias(@PathVariable Long id) {
        return "redirect:/lots/mouvements/detail/" + id;
    }

    @PostMapping("/lots/mouvements/save")
    public String saveMouvement(@ModelAttribute MouvementLotDTO dto, Model model, HttpSession session) {
        String erreur = service.ajouterMouvement(dto, (Long) session.getAttribute("userId"));

        if (erreur != null) {
            model.addAttribute("erreur", erreur);
            prepareFormModel(model, dto);
            return "lots/mouvements/form";
        }

        return "redirect:/lots/mouvements";
    }

    private void prepareFormModel(Model model, MouvementLotDTO dto) {
        model.addAttribute("mouvement", dto);
        model.addAttribute("lots", lotRepository.findAll());
        model.addAttribute("typesMouvement", typeMouvementLotRepository.findAll());
    }
}
