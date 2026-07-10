package com.madaporc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.madaporc.dto.RapportFiltreDTO;
import com.madaporc.dto.RapportFinancierDTO;
import com.madaporc.service.ImportExportService;
import com.madaporc.service.RapportService;

@Controller
public class RapportController {

    private final RapportService rapportService;
    private final ImportExportService importExportService;

    public RapportController(RapportService rapportService, ImportExportService importExportService) {
        this.rapportService = rapportService;
        this.importExportService = importExportService;
    }

    // Page Rapports : affiche le rapport financier filtre par date (debut / fin).
    @GetMapping("/rapports")
    public String index(@ModelAttribute RapportFiltreDTO filtre, Model model) {
        model.addAttribute("filtre", filtre);
        try {
            RapportFinancierDTO rapport = rapportService.genererRapport(filtre);
            model.addAttribute("rapport", rapport);
        } catch (IllegalArgumentException e) {
            model.addAttribute("erreur", e.getMessage());
            model.addAttribute("rapport", new RapportFinancierDTO());
        }
        return "rapports/index";
    }

    // Rapports PDF par domaine (reutilisent la machinerie d'export existante).
    @GetMapping("/rapports/sanitaire/pdf")
    public ResponseEntity<byte[]> sanitairePdf() {
        return pdf("SANITAIRE", "sanitaire");
    }

    @GetMapping("/rapports/commercial/pdf")
    public ResponseEntity<byte[]> commercialPdf() {
        return pdf("VENTES", "commercial");
    }

    @GetMapping("/rapports/financier/pdf")
    public ResponseEntity<byte[]> financierPdf() {
        return pdf("FINANCIER", "financier");
    }

    @GetMapping("/rapports/reproduction/pdf")
    public ResponseEntity<byte[]> reproductionPdf() {
        return pdf("GROUPES", "reproduction");
    }

    // Export global : toutes les donnees dans un CSV (ouvrable dans Excel).
    @GetMapping("/rapports/export/excel")
    public ResponseEntity<byte[]> exportGlobal() {
        byte[] data = importExportService.exporterCsvGlobal();
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return fichier(data, "export_global.csv", "text/csv");
    }

    private ResponseEntity<byte[]> pdf(String module, String nom) {
        byte[] data = importExportService.exporterPdf(module);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return fichier(data, "rapport_" + nom + ".pdf", "application/pdf");
    }

    private ResponseEntity<byte[]> fichier(byte[] data, String nom, String type) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nom + "\"")
                .contentType(MediaType.parseMediaType(type))
                .body(data);
    }
}
