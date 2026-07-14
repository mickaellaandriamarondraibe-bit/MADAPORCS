package com.madaporc.controller;

import com.madaporc.dto.ImportExcelDTO;
import com.madaporc.service.ImportExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ImportExportController {

    private final ImportExportService service;

    public ImportExportController(ImportExportService service) {
        this.service = service;
    }

    @GetMapping("/imports")
    public String indexImports() {
        return "imports/form";
    }

    @GetMapping("/imports-exports/historique")
    public String historique(Model model) {
        model.addAttribute("historique", service.historique());
        return "imports/historique";
    }

    @PostMapping("/imports/excel")
    public String importerExcel(@ModelAttribute ImportExcelDTO dto, RedirectAttributes ra) {
        String resultat = service.importerCsv(dto.getFile(), dto.getModule());
        // "success" est affiché par le bandeau d'alerte standard (header),
        // contrairement à "message" : le rapport d'import devient donc visible.
        ra.addFlashAttribute("success", resultat);
        return "redirect:/imports-exports/historique";
    }

    // Modele telechargeable : en-tetes attendus + un exemple
    @GetMapping("/imports/modele")
    public ResponseEntity<byte[]> modele(@RequestParam String module) {
        byte[] data = service.modeleCsv(module);
        if (data == null) return ResponseEntity.notFound().build();
        return fichier(data, "modele_" + module.toLowerCase() + ".csv", "text/csv");
    }

    @GetMapping("/exports/excel")
    public ResponseEntity<byte[]> exporterExcel(@RequestParam String module) {
        byte[] data = service.exporterXlsx(module);
        if (data == null) return ResponseEntity.notFound().build();
        return fichier(data, module.toLowerCase() + ".xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @GetMapping("/exports/pdf")
    public ResponseEntity<byte[]> exporterPdf(@RequestParam String module) {
        byte[] data = service.exporterPdf(module);
        if (data == null) return ResponseEntity.notFound().build();
        return fichier(data, module.toLowerCase() + ".pdf", "application/pdf");
    }

    private ResponseEntity<byte[]> fichier(byte[] data, String nom, String type) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nom + "\"")
                .contentType(MediaType.parseMediaType(type))
                .body(data);
    }
}
