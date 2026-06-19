package com.madaporc.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.DTO.LotPorcDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.service.LotPorcService;
import com.madaporc.service.PdfExportService;

@Controller
public class LotPorcController {
    private final LotPorcService service;
    private final PdfExportService pdfExportService;

    public LotPorcController(LotPorcService service, PdfExportService pdfExportService) {
        this.service = service;
        this.pdfExportService = pdfExportService;
    }

    @GetMapping("/lots")
    public String listLots(
            @RequestParam(required = false) String motCle,
            @RequestParam(required = false) Long raceId,
            @RequestParam(required = false) Long statutId,
            @RequestParam(required = false) String date,
            Model model
    ) {
        LocalDate filterDate = date == null || date.isBlank() ? null : LocalDate.parse(date);
        service.prepareLotListModel(model, motCle, raceId, statutId, filterDate);
        model.addAttribute("motCle", motCle);
        model.addAttribute("raceId", raceId);
        model.addAttribute("statutId", statutId);
        model.addAttribute("date", date);
        model.addAttribute("totalLots", service.countLots());
        model.addAttribute("totalLotsActifs", service.countLotsActifs());
        model.addAttribute("lotsEnAlerte", service.countLotsEnAlerte());
        model.addAttribute("lotsNouveauxCeMois", service.countLotsNouveauxCeMois());
        return "lots/list";
    }

    @GetMapping("/lots/export")
    public ResponseEntity<byte[]> exportLots(@RequestParam(required = false) String motCle,
                                             @RequestParam(required = false) Long raceId,
                                             @RequestParam(required = false) Long statutId,
                                             @RequestParam(required = false) String date) {
        LocalDate filterDate = date == null || date.isBlank() ? null : LocalDate.parse(date);
        List<LotPorc> filteredLots = service.rechercherLots(motCle, raceId, statutId, filterDate);
        Map<Long, String> raceLibelles = service.buildRaceLibelles();
        Map<Long, String> statutLibelles = service.buildStatutLibelles();
        long total = filteredLots.size();
        long actifs = filteredLots.stream().filter(l -> l.getArchivedAt() == null).count();
        long alertes = filteredLots.stream()
                .filter(l -> l.getArchivedAt() == null)
                .filter(l -> l.getNombreActuel() != null && l.getNombreActuel() <= 5)
                .count();
        String dateLabel = date == null || date.isBlank() ? "Toutes" : date;
        String motCleLabel = motCle == null || motCle.isBlank() ? "Tous" : motCle;
        String raceLabel = raceId != null ? raceLibelles.getOrDefault(raceId, "Tous") : "Tous";
        String statutLabel = statutId != null ? statutLibelles.getOrDefault(statutId, "Tous") : "Tous";
        byte[] pdfBytes = pdfExportService.renderLotExportPdf(filteredLots, raceLibelles, statutLibelles,
                "Export PDF des lots", dateLabel, total, actifs, alertes, motCleLabel, raceLabel, statutLabel);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "lots-export.pdf");
        headers.setContentLength(pdfBytes.length);
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
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
        return "lots/form";
    }

    @PostMapping("/lots/save")
    public String saveLot(@ModelAttribute LotPorcDTO dto, Model model, HttpSession session) {
        Long u = (Long) session.getAttribute("userId");
        String e = dto.getId() == null ? service.creer(dto, u) : service.modifier(dto.getId(), dto);
        model.addAttribute("message", e == null ? "Lot enregistré." : e);
        service.prepareLotFormModel(model, dto.getId(), dto.getTypeEntree());
        return "lots/form";
    }

   @GetMapping("/lots/detail/{id}")
public String detailLot(@PathVariable Long id, Model model) {
    model.addAttribute("lot", service.getDetailLot(id));
    return "lots/detail";
}

@GetMapping("/lots/detail/{id}/tab/{tab}")
public String detailLotTab(
        @PathVariable Long id,
        @PathVariable String tab,
        Model model
) {
    model.addAttribute("lot", service.getDetailLot(id));
    model.addAttribute("tab", tab);
    return "lots/detail";
}
}
