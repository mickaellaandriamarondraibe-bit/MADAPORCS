package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.madaporc.DTO.ReproducteurDTO;
import com.madaporc.DTO.ReproducteurDetailDTO;
import com.madaporc.model.EvenementReproduction;
import com.madaporc.model.Reproducteur;
import com.madaporc.model.SuiviSanitaire;
import com.madaporc.model.Vaccination;
import com.madaporc.repository.EvenementReproductionRepository;
import com.madaporc.repository.RaceRepository;
import com.madaporc.repository.ReproducteurRepository;
import com.madaporc.repository.SexeRepository;
import com.madaporc.repository.StatutReproducteurRepository;
import com.madaporc.repository.SuiviSanitaireRepository;
import com.madaporc.repository.VaccinationRepository;

@Service
public class ReproducteurService {

    private final ReproducteurRepository repo;
    private final RaceRepository raceRepo;
    private final SexeRepository sexeRepo;
    private final StatutReproducteurRepository statutRepo;
    private final EvenementReproductionRepository evtRepo;
    private final SuiviSanitaireRepository suiviRepo;
    private final VaccinationRepository vaccinRepo;

    public ReproducteurService(
            ReproducteurRepository repo,
            RaceRepository raceRepo,
            SexeRepository sexeRepo,
            StatutReproducteurRepository statutRepo,
            EvenementReproductionRepository evtRepo,
            SuiviSanitaireRepository suiviRepo,
            VaccinationRepository vaccinRepo
    ) {
        this.repo = repo;
        this.raceRepo = raceRepo;
        this.sexeRepo = sexeRepo;
        this.statutRepo = statutRepo;
        this.evtRepo = evtRepo;
        this.suiviRepo = suiviRepo;
        this.vaccinRepo = vaccinRepo;
    }


    public List<Reproducteur> rechercherReproducteurs(String motCle, Long sexeId, Long statutId) {
        return repo.findAll()
                .stream()
                .filter(r -> motCle == null || motCle.isBlank()
                        || contient(r.getCodeReproducteur(), motCle)
                        || contient(r.getNom(), motCle))
                .filter(r -> sexeId == null || sexeId.equals(r.getSexeId()))
                .filter(r -> statutId == null || statutId.equals(r.getStatutReproducteurId()))
                .filter(r -> r.getArchivedAt() == null)
                .toList();
    }


    public void prepareReproducteurFormModel(Model model, Long id) {
        ReproducteurDTO dto = new ReproducteurDTO();

        if (id != null) {
            repo.findById(id).ifPresent(r -> {
                ReproducteurDTO converted = dto(r);

                dto.setId(converted.getId());
                dto.setCodeReproducteur(converted.getCodeReproducteur());
                dto.setNom(converted.getNom());
                dto.setRaceId(converted.getRaceId());
                dto.setSexeId(converted.getSexeId());
                dto.setStatutReproducteurId(converted.getStatutReproducteurId());
                dto.setDateNaissance(converted.getDateNaissance());
                dto.setDateArrivee(converted.getDateArrivee());
                dto.setPrixAchat(converted.getPrixAchat());
                dto.setPoidsKg(converted.getPoidsKg());
                dto.setObservation(converted.getObservation());
            });
        }

        model.addAttribute("reproducteur", dto);
        model.addAttribute("races", raceRepo.findAll());
        model.addAttribute("sexes", sexeRepo.findAll());
        model.addAttribute("statuts", statutRepo.findAll());
    }


    public String creer(ReproducteurDTO dto, Long uid) {
        String erreur = validerReproducteur(dto);

        if (erreur != null) {
            return erreur;
        }

        erreur = verifierCodeUnique(dto.getCodeReproducteur(), null);

        if (erreur != null) {
            return erreur;
        }

        Reproducteur r = ent(dto);

        r.setCreatedBy(uid);
        r.setCreatedAt(LocalDateTime.now());

        repo.save(r);

        return null;
    }


    public String modifier(Long id, ReproducteurDTO dto) {
        if (id == null) {
            return "Identifiant invalide.";
        }

        if (!repo.existsById(id)) {
            return "Reproducteur introuvable.";
        }

        String erreur = validerReproducteur(dto);

        if (erreur != null) {
            return erreur;
        }

        erreur = verifierCodeUnique(dto.getCodeReproducteur(), id);

        if (erreur != null) {
            return erreur;
        }

        Reproducteur r = ent(dto);

        r.setId(id);

        repo.save(r);

        return null;
    }


    public String archiverReproducteur(Long id) {
        Reproducteur r = repo.findById(id).orElse(null);

        if (r == null) {
            return "Reproducteur introuvable.";
        }

        r.setArchivedAt(LocalDateTime.now());

        repo.save(r);

        return null;
    }


    public String verifierCodeUnique(String code, Long idActuel) {
        if (code == null || code.isBlank()) {
            return "Code reproducteur obligatoire.";
        }

        Optional<Reproducteur> r = repo.findByCodeReproducteur(code);

        if (r.isEmpty()) {
            return null;
        }

        if (idActuel != null && r.get().getId().equals(idActuel)) {
            return null;
        }

        return "Code reproducteur déjà utilisé.";
    }


    public ReproducteurDetailDTO getDetailReproducteur(Long id) {
        ReproducteurDetailDTO detail = new ReproducteurDetailDTO();

        repo.findById(id).ifPresent(r -> {
            detail.setId(r.getId());
            detail.setCodeReproducteur(r.getCodeReproducteur());
        });

        return detail;
    }


    public List<EvenementReproduction> getEvenements(Long id) {
        return evtRepo.findByFemelleIdOrMaleIdOrderByDateEvenementDesc(id, id);
    }


    public List<SuiviSanitaire> getSuivisSanitaires(Long id) {
        return suiviRepo.findByReproducteurId(id);
    }


    public List<Vaccination> getVaccinations(Long id) {
        return vaccinRepo.findByReproducteurId(id);
    }


    public Optional<EvenementReproduction> getCycleActuel(Long id) {
        return getEvenements(id)
                .stream()
                .findFirst();
    }


    public String validerReproducteur(ReproducteurDTO dto) {
        if (dto == null) {
            return "Reproducteur obligatoire.";
        }

        if (dto.getCodeReproducteur() == null || dto.getCodeReproducteur().isBlank()) {
            return "Code reproducteur obligatoire.";
        }

        if (dto.getRaceId() == null) {
            return "Race obligatoire.";
        }

        if (dto.getSexeId() == null) {
            return "Sexe obligatoire.";
        }

        if (dto.getStatutReproducteurId() == null) {
            return "Statut obligatoire.";
        }

        if (dto.getPoidsKg() != null && dto.getPoidsKg().compareTo(BigDecimal.ZERO) <= 0) {
            return "Le poids doit être supérieur à 0.";
        }

        return null;
    }


    private Reproducteur ent(ReproducteurDTO d) {
        Reproducteur r = new Reproducteur();

        r.setId(d.getId());
        r.setCodeReproducteur(d.getCodeReproducteur());
        r.setNom(d.getNom());
        r.setRaceId(d.getRaceId());
        r.setSexeId(d.getSexeId());
        r.setStatutReproducteurId(d.getStatutReproducteurId());
        r.setDateNaissance(d.getDateNaissance());
        r.setDateArrivee(d.getDateArrivee());
        r.setPrixAchat(d.getPrixAchat());
        r.setPoidsKg(d.getPoidsKg());
        r.setObservation(d.getObservation());

        return r;
    }


    private ReproducteurDTO dto(Reproducteur r) {
        ReproducteurDTO d = new ReproducteurDTO();

        d.setId(r.getId());
        d.setCodeReproducteur(r.getCodeReproducteur());
        d.setNom(r.getNom());
        d.setRaceId(r.getRaceId());
        d.setSexeId(r.getSexeId());
        d.setStatutReproducteurId(r.getStatutReproducteurId());
        d.setDateNaissance(r.getDateNaissance());
        d.setDateArrivee(r.getDateArrivee());
        d.setPrixAchat(r.getPrixAchat());
        d.setPoidsKg(r.getPoidsKg());
        d.setObservation(r.getObservation());

        return d;
    }


    private boolean contient(String valeur, String motCle) {
        return valeur != null
                && motCle != null
                && valeur.toLowerCase().contains(motCle.toLowerCase());
    }
}