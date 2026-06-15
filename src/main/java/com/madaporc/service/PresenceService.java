package com.madaporc.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.DTO.PresenceDTO;
import com.madaporc.model.Presence;
import com.madaporc.repository.EmployeRepository;
import com.madaporc.repository.PresenceRepository;

@Service
public class PresenceService {

    private final PresenceRepository presenceRepository;
    private final EmployeRepository employeRepository;

    public PresenceService(
            PresenceRepository presenceRepository,
            EmployeRepository employeRepository
    ) {
        this.presenceRepository = presenceRepository;
        this.employeRepository = employeRepository;
    }


    public List<Presence> findPresences(LocalDate date) {
        if (date == null) {
            return presenceRepository.findAll();
        }

        return presenceRepository.findByDatePresence(date);
    }


    public String enregistrerPresence(PresenceDTO dto) {
        String erreur = validerPresence(dto);

        if (erreur != null) {
            return erreur;
        }

        if (dto.getId() == null && verifierPresenceDuJour(dto.getEmployeId(), dto.getDatePresence())) {
            return "Cet employé a déjà un pointage pour cette date.";
        }

        Presence presence = convertirDtoVersEntity(dto);

        presenceRepository.save(presence);

        return null;
    }


    public String pointerEmploye(Long employeId, String statut, LocalTime heure) {
        if (employeId == null) {
            return "Employé obligatoire.";
        }

        if (!employeRepository.existsById(employeId)) {
            return "Employé introuvable.";
        }

        LocalDate aujourdHui = LocalDate.now();

        if (verifierPresenceDuJour(employeId, aujourdHui)) {
            return "L'employé a déjà été pointé aujourd'hui.";
        }

        Presence presence = new Presence();

        presence.setEmployeId(employeId);
        presence.setDatePresence(aujourdHui);
        presence.setStatutPresence(statut);
        presence.setHeureArrivee(heure == null ? LocalTime.now() : heure);

        presenceRepository.save(presence);

        return null;
    }


    public boolean verifierPresenceDuJour(Long employeId, LocalDate date) {
        if (employeId == null || date == null) {
            return false;
        }

        return presenceRepository.existsByEmployeIdAndDatePresence(employeId, date);
    }


    public long calculerNombreJoursPresence(Long employeId, int mois, int annee) {
        LocalDate debut = LocalDate.of(annee, mois, 1);
        LocalDate fin = debut.withDayOfMonth(debut.lengthOfMonth());

        return presenceRepository.findByEmployeIdAndDatePresenceBetween(employeId, debut, fin)
                .stream()
                .filter(p -> estStatut(p.getStatutPresence(), "Présent")
                        || estStatut(p.getStatutPresence(), "Present"))
                .count();
    }


    public long calculerAbsences(Long employeId, int mois, int annee) {
        LocalDate debut = LocalDate.of(annee, mois, 1);
        LocalDate fin = debut.withDayOfMonth(debut.lengthOfMonth());

        return presenceRepository.findByEmployeIdAndDatePresenceBetween(employeId, debut, fin)
                .stream()
                .filter(p -> estStatut(p.getStatutPresence(), "Absent"))
                .count();
    }


    public long calculerRetards(Long employeId, int mois, int annee) {
        LocalDate debut = LocalDate.of(annee, mois, 1);
        LocalDate fin = debut.withDayOfMonth(debut.lengthOfMonth());

        return presenceRepository.findByEmployeIdAndDatePresenceBetween(employeId, debut, fin)
                .stream()
                .filter(p -> estStatut(p.getStatutPresence(), "Retard"))
                .count();
    }


    public String validerPresence(PresenceDTO dto) {
        if (dto == null) {
            return "Présence obligatoire.";
        }

        if (dto.getEmployeId() == null) {
            return "Employé obligatoire.";
        }

        if (!employeRepository.existsById(dto.getEmployeId())) {
            return "Employé introuvable.";
        }

        if (dto.getDatePresence() == null) {
            return "Date de présence obligatoire.";
        }

        if (dto.getDatePresence().isAfter(LocalDate.now())) {
            return "La date de présence ne peut pas être dans le futur.";
        }

        if (dto.getStatutPresence() == null || dto.getStatutPresence().isBlank()) {
            return "Statut de présence obligatoire.";
        }

        if (dto.getHeureArrivee() != null
                && dto.getHeureDepart() != null
                && dto.getHeureDepart().isBefore(dto.getHeureArrivee())) {
            return "L'heure de départ doit être après l'heure d'arrivée.";
        }

        return null;
    }


    public Presence convertirDtoVersEntity(PresenceDTO dto) {
        Presence presence = new Presence();

        presence.setId(dto.getId());
        presence.setEmployeId(dto.getEmployeId());
        presence.setDatePresence(dto.getDatePresence());
        presence.setStatutPresence(dto.getStatutPresence());
        presence.setHeureArrivee(dto.getHeureArrivee());
        presence.setHeureDepart(dto.getHeureDepart());
        presence.setObservation(dto.getObservation());

        return presence;
    }


    private boolean estStatut(String valeur, String statut) {
        return valeur != null && valeur.equalsIgnoreCase(statut);
    }
}