package com.madaporc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madaporc.dto.EvenementCalendrier;
import com.madaporc.model.Depense;
import com.madaporc.model.GroupeReproduction;
import com.madaporc.model.MouvementLotPorc;
import com.madaporc.model.Vente;
import com.madaporc.repository.DepenseRepository;
import com.madaporc.repository.GroupeReproductionRepository;
import com.madaporc.repository.MouvementLotPorcRepository;
import com.madaporc.repository.VenteRepository;

@Service
public class CalendrierService {

    private final GroupeReproductionRepository groupeRepo;
    private final DepenseRepository depenseRepo;
    private final VenteRepository venteRepo;
    private final MouvementLotPorcRepository mouvementRepo;

    public CalendrierService(GroupeReproductionRepository groupeRepo,
                            DepenseRepository depenseRepo,
                            VenteRepository venteRepo,
                            MouvementLotPorcRepository mouvementRepo) {
        this.groupeRepo = groupeRepo;
        this.depenseRepo = depenseRepo;
        this.venteRepo = venteRepo;
        this.mouvementRepo = mouvementRepo;
    }

    /** Rassemble tous les événements datés du projet dans un format commun. */
    // Transaction ouverte pour charger le lot (LAZY) des mouvements DECES,
    // sinon l'accès à m.getLot() échoue (open-in-view désactivé).
    @Transactional(readOnly = true)
    public List<EvenementCalendrier> tousLesEvenements() {
        List<EvenementCalendrier> events = new ArrayList<>();

        // Mises bas prévues
        for (GroupeReproduction g : groupeRepo.findAll()) {
            if (g.getDatePrevueMiseBas() != null) {
                events.add(new EvenementCalendrier(
                        "Mise bas " + g.getCodeGroupe(),
                        g.getDatePrevueMiseBas().toString(),
                        "misebas", "#e0a800"));
            }
        }

        // Dépenses
        for (Depense d : depenseRepo.findAllByOrderByDateDepenseDesc()) {
            if (d.getDateDepense() != null) {
                events.add(new EvenementCalendrier(
                        "Dépense : " + d.getMontant() + " Ar",
                        d.getDateDepense().toString(),
                        "depense", "#e34948"));
            }
        }

        // Ventes
        for (Vente v : venteRepo.findAll()) {
            if (v.getDateVente() != null) {
                events.add(new EvenementCalendrier(
                        "Vente : " + v.getMontantTotal() + " Ar",
                        v.getDateVente().toString(),
                        "vente", "#1baf7a"));
            }
        }

        // Morts (mouvement de type DECES)
        for (MouvementLotPorc m : mouvementRepo.findAll()) {
            if ("DECES".equals(m.getTypeMouvement()) && m.getDateMouvement() != null) {
                String lot = m.getLot() != null ? m.getLot().getCodeLot() : "";
                events.add(new EvenementCalendrier(
                        "Mort : " + m.getQuantite() + " (" + lot + ")",
                        m.getDateMouvement().toString(),
                        "mort", "#6b7480"));
            }
        }

        return events;
    }
}
