package com.madaporc.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.madaporc.DTO.MaladieDTO;
import com.madaporc.model.Maladie;
import com.madaporc.repository.MaladieRepository;

@Service
public class MaladieService{
    private final MaladieRepository maladieRepository;

    public MaladieService(MaladieRepository maladieRepository){
        this.maladieRepository=maladieRepository;
    }

    public List<Maladie> rechercherMaladies(String motCle){
        if(motCle!=null && !motCle.isBlank())return maladieRepository.findByLibelleContainingIgnoreCase(motCle);
        return maladieRepository.findAll();
    }

    public void prepareMaladieFormModel(Model model,Long id){
        MaladieDTO dto=new MaladieDTO();
        if(id!=null){
            maladieRepository.findById(id).ifPresent(m->{
                dto.setId(m.getId());
                dto.setLibelle(m.getLibelle());
                dto.setDescription(m.getDescription());
            });
        }
        model.addAttribute("maladie",dto);
    }

    public String creer(MaladieDTO dto){
        String e=validerMaladie(dto);
        if(e!=null)return e;
        maladieRepository.save(toEntity(dto));
        return null;
    }

    public String modifier(Long id,MaladieDTO dto){
        if(id==null)return "Identifiant maladie invalide.";
        if(!maladieRepository.existsById(id))return "Maladie introuvable.";
        String e=validerMaladie(dto);
        if(e!=null)return e;
        Maladie m=toEntity(dto);
        m.setId(id);
        maladieRepository.save(m);
        return null;
    }

    public String validerMaladie(MaladieDTO dto){
        if(dto==null)return "Maladie obligatoire.";
        if(dto.getLibelle()==null || dto.getLibelle().isBlank())return "Libellé obligatoire.";
        return null;
    }

    private Maladie toEntity(MaladieDTO dto){
        Maladie m=new Maladie();
        m.setId(dto.getId());
        m.setLibelle(dto.getLibelle());
        m.setDescription(dto.getDescription());
        return m;
    }
}