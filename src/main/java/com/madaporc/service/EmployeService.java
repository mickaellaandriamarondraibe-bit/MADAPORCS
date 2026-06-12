package com.madaporc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.model.Employe;
import com.madaporc.repository.EmployeRepository;


@Service
public class EmployeService {

    private final EmployeRepository employeRepository;

    public EmployeService(EmployeRepository employeRepository){
        this.employeRepository = employeRepository;

    }
    public List<Employe> findAllEmployes(){
        return employeRepository.findAll();
    }

}
