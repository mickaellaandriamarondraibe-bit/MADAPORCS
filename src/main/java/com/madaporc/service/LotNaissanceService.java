package com.madaporc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.dto.LotNaissanceDTO;
import com.madaporc.model.GroupeReproduction;
import com.madaporc.repository.GroupeReproductionRepository;
import com.madaporc.repository.LotPorcRepository;

@Service
public class LotNaissanceService {

    // private final LotPorcRepository repository;

    public String creerLotNaissanceApresMiseBas(LotNaissanceDTO dto){
        
        return("direct");
    }
    
    
   
}