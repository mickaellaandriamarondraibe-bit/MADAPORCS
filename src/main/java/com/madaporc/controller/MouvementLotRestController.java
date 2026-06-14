package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.madaporc.DTO.LotPorcDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.service.LotPorcService;
import com.madaporc.repository.LotPorcRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/api/lots/mouvements")
public class MouvementLotRestController {
    @Autowired
    private LotPorcService lotPorcService;
    @Autowired
    private LotPorcRepository lotPorcRepository;
    
    @GetMapping("/{lotPorcId}")
    public LotPorcDTO getLotPorc(@PathVariable Long lotPorcId) {
        LotPorc lotPorc = lotPorcRepository.findById(lotPorcId).orElse(null);
        LotPorcDTO dto = lotPorcService.convertToDTO(lotPorc);
        return dto;
    }
}
