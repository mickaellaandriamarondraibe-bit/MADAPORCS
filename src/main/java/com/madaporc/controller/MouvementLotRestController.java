package com.madaporc.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.madaporc.DTO.MouvementLotDTO;
import com.madaporc.service.MouvementLotService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/lots")
public class MouvementLotRestController {

    private final MouvementLotService mouvementLotService;

    public MouvementLotRestController(MouvementLotService mouvementLotService) {
        this.mouvementLotService = mouvementLotService;
    }

    @GetMapping("/{lotId}/mouvements")
    public ResponseEntity<?> lister(@PathVariable Long lotId) {
        return ResponseEntity.ok(mouvementLotService.findMouvements(lotId));
    }

    @PostMapping("/mouvements")
    public ResponseEntity<?> ajouter(
            @RequestBody MouvementLotDTO dto,
            HttpSession session
    ) {
        Long utilisateurId = (Long) session.getAttribute("userId");

        String erreur = mouvementLotService.ajouterMouvement(dto, utilisateurId);

        if (erreur != null) {
            return ResponseEntity.badRequest().body(Map.of("erreur", erreur));
        }

        return ResponseEntity.ok(Map.of("message", "Mouvement enregistré avec succès."));
    }
}