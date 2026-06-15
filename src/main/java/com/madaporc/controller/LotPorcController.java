package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.DTO.LotPorcDTO;
import com.madaporc.service.LotPorcService;

@Controller
public class LotPorcController {
    private final LotPorcService service;

    public LotPorcController(LotPorcService service) {
        this.service = service;
    }

    @GetMapping("/lots")
    public String listLots(@RequestParam(required = false) String code, @RequestParam(required = false) Long raceId,
            @RequestParam(required = false) Long statutId, Model model) {
        service.prepareLotListModel(model, code, raceId, statutId);
        return "lots/listeLots";
    }

    @PostMapping("/lots/archive/{id}")
    public String archiver(@PathVariable Long id, Model model) {
        service.archiverLot(id);
        return "redirect:/lots";
    }

    @GetMapping("/lots/form")
    public String showForm(@RequestParam(required = false) Long id, @RequestParam(required = false) String typeEntree,
            Model model) {
        service.prepareLotFormModel(model, id, typeEntree);
        return "lots/formLot";
    }

    @PostMapping("/lots/save")
    public String saveLot(@ModelAttribute LotPorcDTO dto, Model model, HttpSession session) {
        Long u = (Long) session.getAttribute("userId");
        String e = dto.getId() == null ? service.creer(dto, u) : service.modifier(dto.getId(), dto);
        model.addAttribute("message", e == null ? "Lot enregistré." : e);
        service.prepareLotFormModel(model, dto.getId(), dto.getTypeEntree());
        return "lots/formLot";
    }

   @GetMapping("/lots/detail/{id}")
public String detailLot(@PathVariable Long id, Model model) {
    model.addAttribute("lot", service.getDetailLot(id));
    return "lots/detailLot";
}

@GetMapping("/lots/detail/{id}/tab/{tab}")
public String detailLotTab(
        @PathVariable Long id,
        @PathVariable String tab,
        Model model
) {
    model.addAttribute("lot", service.getDetailLot(id));
    model.addAttribute("tab", tab);
    return "lots/detailLot";
}
}
