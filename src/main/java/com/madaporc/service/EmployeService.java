package com.madaporc.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
    public List<Employe> findAllEmployes(){
        return employeRepository.findAll();
    }

    public Employe findEmployeById(Long id) {
        return employeRepository.findById(id).orElseThrow();
    }

    public void saveEmploye(Employe employe, Long posteEmployeId, Long statutEmployeId) {
        PosteEmploye posteEmploye = null;
        StatutEmploye statutEmploye = null;

        if (posteEmployeId != null) {
            posteEmploye = posteEmployeRepository.findById(posteEmployeId).orElse(null);
        }

        if (statutEmployeId != null) {
            statutEmploye = statutEmployeRepository.findById(statutEmployeId).orElse(null);
        }

        employe.setPosteEmploye(posteEmploye);
        employe.setStatutEmploye(statutEmploye);

        employeRepository.save(employe);
    }

    public void deleteEmploye(Long id) {
        employeRepository.deleteById(id);
    }

    public void desactiverEmploye(Long id) {
        Employe employe = findEmployeById(id);
        StatutEmploye statutEmploye = statutEmployeRepository.findByLibelleIgnoreCase("Inactif").orElseGet(() -> {
            StatutEmploye statut = new StatutEmploye();
            statut.setLibelle("Inactif");
            return statutEmployeRepository.save(statut);
        });

        employe.setStatutEmploye(statutEmploye);
        employeRepository.save(employe);
    }
}
