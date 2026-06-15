package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.DTO.FicheSalaireDTO;
import com.madaporc.DTO.SalaireEmployeDTO;
import com.madaporc.model.Employe;
import com.madaporc.model.Presence;
import com.madaporc.model.SalaireEmploye;
import com.madaporc.repository.EmployeRepository;
import com.madaporc.repository.PresenceRepository;
import com.madaporc.repository.SalaireEmployeRepository;

@Service
public class SalaireService {

    private final SalaireEmployeRepository salaireRepository;
    private final EmployeRepository employeRepository;
    private final PresenceRepository presenceRepository;

    public SalaireService(
            SalaireEmployeRepository salaireRepository,
            EmployeRepository employeRepository,
            PresenceRepository presenceRepository
    ) {
        this.salaireRepository = salaireRepository;
        this.employeRepository = employeRepository;
        this.presenceRepository = presenceRepository;
    }


    public List<SalaireEmploye> findSalaires(Integer mois, Integer annee) {
        if (mois == null || annee == null) {
            return salaireRepository.findAll();
        }

        return salaireRepository.findByMoisAndAnnee(mois, annee);
    }


    public String genererSalairesMois(Integer mois, Integer annee) {
        if (mois == null || annee == null) {
            return "Mois et année obligatoires.";
        }

        List<Employe> employes = employeRepository.findAll();

        for (Employe employe : employes) {
            boolean existe = salaireRepository.existsByEmployeIdAndMoisAndAnnee(
                    employe.getId(),
                    mois,
                    annee
            );

            if (!existe) {
                BigDecimal base = nz(employe.getSalaireBase());
                BigDecimal prime = calculerPrime(employe.getId(), mois, annee);
                BigDecimal retenue = calculerRetenue(employe.getId(), mois, annee);
                BigDecimal net = calculerNet(base, prime, retenue);

                SalaireEmploye salaire = new SalaireEmploye();

                salaire.setEmployeId(employe.getId());
                salaire.setMois(mois);
                salaire.setAnnee(annee);
                salaire.setMontantBase(base);
                salaire.setPrime(prime);
                salaire.setRetenue(retenue);
                salaire.setMontantNet(net);
                salaire.setStatutPaiement("En attente");

                salaireRepository.save(salaire);
            }
        }

        return null;
    }


    public String creerOuModifier(SalaireEmployeDTO dto) {
        String erreur = validerSalaire(dto);

        if (erreur != null) {
            return erreur;
        }

        SalaireEmploye salaire = new SalaireEmploye();

        salaire.setId(dto.getId());
        salaire.setEmployeId(dto.getEmployeId());
        salaire.setMois(dto.getMois());
        salaire.setAnnee(dto.getAnnee());
        salaire.setMontantBase(nz(dto.getMontantBase()));
        salaire.setPrime(nz(dto.getPrime()));
        salaire.setRetenue(nz(dto.getRetenue()));
        salaire.setMontantNet(calculerNet(
                dto.getMontantBase(),
                dto.getPrime(),
                dto.getRetenue()
        ));
        salaire.setDatePaiement(dto.getDatePaiement());
        salaire.setStatutPaiement(dto.getStatutPaiement());

        salaireRepository.save(salaire);

        return null;
    }


    public BigDecimal calculerPrime(Long employeId, Integer mois, Integer annee) {
        if (employeId == null || mois == null || annee == null) {
            return BigDecimal.ZERO;
        }

        long joursPresence = calculerNombreJoursPresence(employeId, mois, annee);

        if (joursPresence >= 26) {
            return BigDecimal.valueOf(50000);
        }

        if (joursPresence >= 22) {
            return BigDecimal.valueOf(25000);
        }

        return BigDecimal.ZERO;
    }


    public BigDecimal calculerRetenue(Long employeId, Integer mois, Integer annee) {
        if (employeId == null || mois == null || annee == null) {
            return BigDecimal.ZERO;
        }

        long absences = calculerAbsences(employeId, mois, annee);

        return BigDecimal.valueOf(absences).multiply(BigDecimal.valueOf(10000));
    }


    public BigDecimal calculerNet(BigDecimal base, BigDecimal prime, BigDecimal retenue) {
        return nz(base).add(nz(prime)).subtract(nz(retenue));
    }


    public String marquerCommePaye(Long salaireId, LocalDate datePaiement) {
        if (salaireId == null) {
            return "Salaire invalide.";
        }

        SalaireEmploye salaire = salaireRepository.findById(salaireId).orElse(null);

        if (salaire == null) {
            return "Salaire introuvable.";
        }

        salaire.setStatutPaiement("Payé");
        salaire.setDatePaiement(datePaiement == null ? LocalDate.now() : datePaiement);

        salaireRepository.save(salaire);

        return null;
    }


    public FicheSalaireDTO genererFicheSalaire(Long salaireId) {
        return new FicheSalaireDTO();
    }


    public String validerSalaire(SalaireEmployeDTO dto) {
        if (dto == null) {
            return "Salaire obligatoire.";
        }

        if (dto.getEmployeId() == null) {
            return "Employé obligatoire.";
        }

        if (!employeRepository.existsById(dto.getEmployeId())) {
            return "Employé introuvable.";
        }

        if (dto.getMois() == null || dto.getMois() < 1 || dto.getMois() > 12) {
            return "Mois invalide.";
        }

        if (dto.getAnnee() == null || dto.getAnnee() < 2000) {
            return "Année invalide.";
        }

        if (dto.getMontantBase() == null || dto.getMontantBase().compareTo(BigDecimal.ZERO) < 0) {
            return "Salaire de base invalide.";
        }

        return null;
    }


    private long calculerNombreJoursPresence(Long employeId, Integer mois, Integer annee) {
        LocalDate debut = LocalDate.of(annee, mois, 1);
        LocalDate fin = debut.withDayOfMonth(debut.lengthOfMonth());

        return presenceRepository.findByEmployeIdAndDatePresenceBetween(employeId, debut, fin)
                .stream()
                .filter(p -> estStatut(p, "Présent") || estStatut(p, "Present"))
                .count();
    }


    private long calculerAbsences(Long employeId, Integer mois, Integer annee) {
        LocalDate debut = LocalDate.of(annee, mois, 1);
        LocalDate fin = debut.withDayOfMonth(debut.lengthOfMonth());

        return presenceRepository.findByEmployeIdAndDatePresenceBetween(employeId, debut, fin)
                .stream()
                .filter(p -> estStatut(p, "Absent"))
                .count();
    }


    private boolean estStatut(Presence presence, String statut) {
        return presence.getStatutPresence() != null
                && presence.getStatutPresence().equalsIgnoreCase(statut);
    }


    private BigDecimal nz(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}