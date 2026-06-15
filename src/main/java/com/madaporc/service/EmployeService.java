package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.madaporc.DTO.EmployeDTO;
import com.madaporc.model.Employe;
import com.madaporc.repository.EmployeRepository;
import com.madaporc.repository.PosteEmployeRepository;
import com.madaporc.repository.StatutEmployeRepository;

@Service
public class EmployeService {

    private final EmployeRepository employeRepository;
    private final PosteEmployeRepository posteEmployeRepository;
    private final StatutEmployeRepository statutEmployeRepository;

    public EmployeService(
            EmployeRepository employeRepository,
            PosteEmployeRepository posteEmployeRepository,
            StatutEmployeRepository statutEmployeRepository
    ) {
        this.employeRepository = employeRepository;
        this.posteEmployeRepository = posteEmployeRepository;
        this.statutEmployeRepository = statutEmployeRepository;
    }


    public List<Employe> rechercherEmployes(String motCle, Long posteId, Long statutId) {
        return employeRepository.findAll()
                .stream()
                .filter(e -> motCle == null || motCle.isBlank()
                        || contient(e.getNom(), motCle)
                        || contient(e.getPrenom(), motCle))
                .filter(e -> posteId == null || posteId.equals(e.getPosteEmployeId()))
                .filter(e -> statutId == null || statutId.equals(e.getStatutEmployeId()))
                .toList();
    }


    public String creer(EmployeDTO dto) {
        String erreur = validerEmploye(dto);

        if (erreur != null) {
            return erreur;
        }

        Employe employe = convertirDtoVersEntity(dto);
        employe.setCreatedAt(LocalDateTime.now());

        employeRepository.save(employe);

        return null;
    }


    public String modifier(Long id, EmployeDTO dto) {
        if (id == null) {
            return "Identifiant employé invalide.";
        }

        if (!employeRepository.existsById(id)) {
            return "Employé introuvable.";
        }

        String erreur = validerEmploye(dto);

        if (erreur != null) {
            return erreur;
        }

        Employe employe = convertirDtoVersEntity(dto);
        employe.setId(id);

        employeRepository.save(employe);

        return null;
    }


    public String archiverEmploye(Long id) {
        Employe employe = employeRepository.findById(id).orElse(null);

        if (employe == null) {
            return "Employé introuvable.";
        }

        employe.setStatutEmployeId(3L);
        employeRepository.save(employe);

        return null;
    }


    public BigDecimal getSalaireBase(Long employeId) {
        return employeRepository.findById(employeId)
                .map(Employe::getSalaireBase)
                .orElse(BigDecimal.ZERO);
    }


    public void prepareEmployeFormModel(Model model, Long id) {
        EmployeDTO dto = new EmployeDTO();

        if (id != null) {
            employeRepository.findById(id).ifPresent(employe -> {
                dto.setId(employe.getId());
                dto.setNom(employe.getNom());
                dto.setPrenom(employe.getPrenom());
                dto.setContact(employe.getContact());
                dto.setAdresse(employe.getAdresse());
                dto.setPosteEmployeId(employe.getPosteEmployeId());
                dto.setStatutEmployeId(employe.getStatutEmployeId());
                dto.setDateEmbauche(employe.getDateEmbauche());
                dto.setSalaireBase(employe.getSalaireBase());
            });
        }

        model.addAttribute("employe", dto);
        model.addAttribute("postes", posteEmployeRepository.findAll());
        model.addAttribute("statuts", statutEmployeRepository.findAll());
    }


    public String validerEmploye(EmployeDTO dto) {
        if (dto == null) {
            return "Employé obligatoire.";
        }

        if (dto.getNom() == null || dto.getNom().isBlank()) {
            return "Nom obligatoire.";
        }

        if (dto.getPosteEmployeId() == null) {
            return "Poste obligatoire.";
        }

        if (dto.getStatutEmployeId() == null) {
            return "Statut obligatoire.";
        }

        if (dto.getSalaireBase() != null && dto.getSalaireBase().compareTo(BigDecimal.ZERO) < 0) {
            return "Le salaire de base ne peut pas être négatif.";
        }

        return null;
    }


    public Employe convertirDtoVersEntity(EmployeDTO dto) {
        Employe employe = new Employe();

        employe.setId(dto.getId());
        employe.setNom(dto.getNom());
        employe.setPrenom(dto.getPrenom());
        employe.setContact(dto.getContact());
        employe.setAdresse(dto.getAdresse());
        employe.setPosteEmployeId(dto.getPosteEmployeId());
        employe.setStatutEmployeId(dto.getStatutEmployeId());
        employe.setDateEmbauche(dto.getDateEmbauche());
        employe.setSalaireBase(dto.getSalaireBase());

        return employe;
    }


    private boolean contient(String valeur, String motCle) {
        return valeur != null
                && motCle != null
                && valeur.toLowerCase().contains(motCle.toLowerCase());
    }
}