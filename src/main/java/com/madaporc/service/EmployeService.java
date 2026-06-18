package com.madaporc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.DTO.EmployeDTO;
import com.madaporc.model.Employe;
import com.madaporc.model.PosteEmploye;
import com.madaporc.model.StatutEmploye;
import com.madaporc.repository.EmployeRepository;
import com.madaporc.repository.PosteEmployeRepository;
import com.madaporc.repository.StatutEmployeRepository;


@Service
public class EmployeService {

    private final EmployeRepository employeRepository;
    private final PosteEmployeRepository posteEmployeRepository;
    private final StatutEmployeRepository statutEmployeRepository;

    public EmployeService(EmployeRepository employeRepository, PosteEmployeRepository posteEmployeRepository, StatutEmployeRepository statutEmployeRepository){
        this.employeRepository = employeRepository;
        this.posteEmployeRepository = posteEmployeRepository;
        this.statutEmployeRepository = statutEmployeRepository;

    }
    public List<Employe> rechercherEmployes(String motCle, Long posteId, Long statutId){
        return employeRepository.findAll();
    }

    public Employe findEmployeById(Long id) {
        return employeRepository.findById(id).orElseThrow();
    }

    public String creer(EmployeDTO dto) {
        Employe employe = new Employe();
        remplirEmployeDepuisDTO(employe, dto);
        employeRepository.save(employe);
        return "employe cree avec succes";
    }

    public String modifier(Long id, EmployeDTO dto){
        Employe employe = employeRepository.findById(id).orElseThrow();
        remplirEmployeDepuisDTO(employe, dto);
        employeRepository.save(employe);
        return "employe modifier";
    }


    public void archiverEmploye(Long id) {
        Employe employe = findEmployeById(id);
        StatutEmploye statutEmploye = statutEmployeRepository.findByLibelleIgnoreCase("Inactif").orElseGet(() -> {
            StatutEmploye statut = new StatutEmploye();
            statut.setLibelle("Inactif");
            return statutEmployeRepository.save(statut);
        });

        employe.setStatutEmploye(statutEmploye);
        employeRepository.save(employe);
    }

    public void remplirEmployeDepuisDTO(Employe employe, EmployeDTO dto){
        employe.setNom(dto.getNom());
        employe.setPrenom(dto.getPrenom());
        employe.setContact(dto.getContact());
        employe.setAdresse(dto.getAdresse());
        employe.setDateEmbauche(dto.getDateEmbauche());
        employe.setSalaireBase(dto.getSalaireBase());

        if(dto.getPosteEmployeId() != null){
            PosteEmploye poste = posteEmployeRepository.findById(dto.getPosteEmployeId()).orElse(null);
            employe.setPosteEmploye(poste);
        }

        if(dto.getStatutEmployeId() != null){
            StatutEmploye statut = statutEmployeRepository.findById(dto.getStatutEmployeId()).orElse(null);
            employe.setStatutEmploye(statut);
        }



    }

}
