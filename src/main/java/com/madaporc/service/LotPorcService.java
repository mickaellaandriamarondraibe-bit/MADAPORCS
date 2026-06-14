package com.madaporc.service;

import com.madaporc.DTO.LotDetailDTO;
import com.madaporc.DTO.LotPorcDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.Race;
import com.madaporc.model.StatutLot;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.RaceRepository;
import com.madaporc.repository.StatutLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LotPorcService {

    @Autowired
    private LotPorcRepository lotPorcRepository;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private StatutLotRepository statutLotRepository;

    public List<LotPorc> rechercherLots(String code, Long raceId, Long statutId) {

        List<LotPorc> lots = lotPorcRepository.findByArchivedAtIsNull();

        return lots.stream()
                .filter(l -> code == null || code.isBlank()
                        || l.getCodeLot().toLowerCase().contains(code.toLowerCase()))
                .filter(l -> raceId == null || raceId.equals(l.getRaceId()))
                .filter(l -> statutId == null || statutId.equals(l.getStatutLotId()))
                .toList();
    }

    public void prepareLotListModel(Model model, String code, Long raceId, Long statutId) {
        model.addAttribute("lots", rechercherLots(code, raceId, statutId));
        model.addAttribute("races", raceRepository.findAll());
        model.addAttribute("statuts", statutLotRepository.findAll());
        model.addAttribute("filtreCode", code);
        model.addAttribute("filtreRaceId", raceId);
        model.addAttribute("filtreStatutId", statutId);
    }

    public void prepareLotFormModel(Model model, Long id, String typeEntree) {
        if (id != null) {
            LotPorc lot = lotPorcRepository.findById(id).orElse(null);
            model.addAttribute("lotPorcDTO", lot);
            model.addAttribute("mode", "modification");
        } else {
            LotPorcDTO dto = new LotPorcDTO();
            dto.setTypeEntree(typeEntree);
            model.addAttribute("lotPorcDTO", dto);
            model.addAttribute("mode", "creation");
        }

        model.addAttribute("races", raceRepository.findAll());
        model.addAttribute("statuts", statutLotRepository.findAll());
    }

    @Transactional
    public String creer(LotPorcDTO dto, Long utilisateurId) {

        String valid = validerDonneesLot(dto);
        if (!valid.equals("valide")) return valid;

        if (lotPorcRepository.existsByCodeLot(dto.getCodeLot())) {
            return "Le code lot existe déjà";
        }

        LotPorc lot = convertirDtoVersEntity(dto);

        lot.setCreatedBy(utilisateurId);
        lot.setCreatedAt(LocalDateTime.now());

        if (lot.getNombreMorts() == null) {
            lot.setNombreMorts(0);
        }
        if (lot.getNombreActuel() == null) {
            lot.setNombreActuel(dto.getNombreInitial());
        }

        lot.setNombreMalesActuel(dto.getNombreMalesInitial());
        lot.setNombreFellesActuel(dto.getNombreFellesInitial());

        lotPorcRepository.save(lot);

        return "Lot créé avec succès";
    }

    @Transactional
    public String modifier(Long id, LotPorcDTO dto) {

        LotPorc lot = lotPorcRepository.findById(id)
                .orElse(null);

        if (lot == null) return "Lot non trouvé";

        String valid = validerDonneesLot(dto);
        if (!valid.equals("valide")) return valid;

        lot.setCodeLot(dto.getCodeLot());
        lot.setTypeEntree(dto.getTypeEntree());
        lot.setRaceId(dto.getRaceId());
        lot.setStatutLotId(dto.getStatutLotId());

        lot.setNombreInitial(dto.getNombreInitial());
        lot.setNombreActuel(dto.getNombreActuel());

        lot.setNombreMalesInitial(dto.getNombreMalesInitial());
        lot.setNombreFellesInitial(dto.getNombreFellesInitial());

        lot.setDateNaissanceEstimee(dto.getDateNaissanceEstimee());
        lot.setDateAchat(dto.getDateAchat());
        lot.setPrixAchatTotal(dto.getPrixAchatTotal());
        lot.setPoidsMoyenInitialKg(dto.getPoidsMoyenInitialKg());
        lot.setObservation(dto.getObservation());

        lotPorcRepository.save(lot);

        return "Lot modifié avec succès";
    }

    @Transactional
    public String archiverLot(Long lotId) {
        LotPorc lot = lotPorcRepository.findById(lotId).orElse(null);

        if (lot == null) return "Lot non trouvé";

        lot.setArchivedAt(LocalDateTime.now());
        lotPorcRepository.save(lot);

        return "Lot archivé avec succès";
    }

    public String validerDonneesLot(LotPorcDTO dto) {

        if (dto.getCodeLot() == null || dto.getCodeLot().isBlank())
            return "Le code lot est obligatoire";

        if (dto.getNombreInitial() == null || dto.getNombreInitial() <= 0)
            return "Le nombre initial doit être > 0";

        if (dto.getNombreActuel() == null || dto.getNombreActuel() < 0)
            return "Le nombre actuel ne peut pas être négatif";

        if (dto.getTypeEntree() == null)
            return "Type entrée obligatoire";

        if (!dto.getTypeEntree().equals("Naissance")
                && !dto.getTypeEntree().equals("Achat"))
            return "Type entrée invalide";

        if (dto.getRaceId() == null)
            return "Race obligatoire";

        if ("Achat".equals(dto.getTypeEntree())) {
            if (dto.getDateAchat() == null)
                return "Date achat obligatoire";
            if (dto.getPrixAchatTotal() == null)
                return "Prix achat obligatoire";
        }

        if ("Naissance".equals(dto.getTypeEntree())
                && dto.getDateNaissanceEstimee() == null) {
            return "Date naissance obligatoire";
        }

        return "valide";
    }

    public LotPorc convertirDtoVersEntity(LotPorcDTO dto) {

        LotPorc lot = new LotPorc();

        lot.setCodeLot(dto.getCodeLot());
        lot.setTypeEntree(dto.getTypeEntree());
        lot.setRaceId(dto.getRaceId());
        lot.setStatutLotId(dto.getStatutLotId());

        lot.setNombreInitial(dto.getNombreInitial());
        lot.setNombreActuel(dto.getNombreActuel());

        lot.setNombreMalesInitial(dto.getNombreMalesInitial());
        lot.setNombreFellesInitial(dto.getNombreFellesInitial());

        lot.setNombreMorts(dto.getNombreMorts() != null ? dto.getNombreMorts() : 0);

        lot.setDateNaissanceEstimee(dto.getDateNaissanceEstimee());
        lot.setDateAchat(dto.getDateAchat());
        lot.setPrixAchatTotal(dto.getPrixAchatTotal());
        lot.setPoidsMoyenInitialKg(dto.getPoidsMoyenInitialKg());
        lot.setObservation(dto.getObservation());

        return lot;
    }


    public List<Object> getMouvements(Long lotId) { return List.of(); }
    public List<Object> getPesees(Long lotId) { return List.of(); }
    public List<Object> getSuivisSanitaires(Long lotId) { return List.of(); }
    public List<Object> getVaccinations(Long lotId) { return List.of(); }
    public List<Object> getDistributions(Long lotId) { return List.of(); }

    public BigDecimal calculerTauxMortalite(Long lotId) {

        LotPorc lot = lotPorcRepository.findById(lotId).orElse(null);
        if (lot == null || lot.getNombreInitial() == 0) return BigDecimal.ZERO;

        BigDecimal morts = new BigDecimal(
                lot.getNombreMorts() == null ? 0 : lot.getNombreMorts()
        );

        return morts
                .divide(new BigDecimal(lot.getNombreInitial()), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
    }

    public BigDecimal calculerGMQMoyen(Long lotId) {

        LotPorc lot = lotPorcRepository.findById(lotId).orElse(null);
        if (lot == null) return BigDecimal.ZERO;

        if (lot.getPoidsMoyenActuelKg() == null
                || lot.getPoidsMoyenInitialKg() == null) {
            return BigDecimal.ZERO;
        }

        return lot.getPoidsMoyenActuelKg()
                .subtract(lot.getPoidsMoyenInitialKg());
    }

    public LotDetailDTO getDetailLot(Long lotId) {

        LotPorc lot = lotPorcRepository.findById(lotId).orElse(null);
        if (lot == null) return null;

        LotDetailDTO dto = new LotDetailDTO();

        dto.setId(lot.getId());
        dto.setCodeLot(lot.getCodeLot());
        dto.setTypeEntree(lot.getTypeEntree());

        dto.setRaceId(lot.getRaceId());
        dto.setStatutLotId(lot.getStatutLotId());

        dto.setNombreInitial(lot.getNombreInitial());
        dto.setNombreActuel(lot.getNombreActuel());

        dto.setNombreMalesInitial(lot.getNombreMalesInitial());
        dto.setNombreFellesInitial(lot.getNombreFellesInitial());

        dto.setNombreMalesActuel(lot.getNombreMalesActuel());
        dto.setNombreFellesActuel(lot.getNombreFellesActuel());

        dto.setNombreMorts(lot.getNombreMorts());

        dto.setDateNaissanceEstimee(lot.getDateNaissanceEstimee());
        dto.setDateAchat(lot.getDateAchat());

        dto.setPrixAchatTotal(lot.getPrixAchatTotal());

        dto.setPoidsMoyenInitialKg(lot.getPoidsMoyenInitialKg());
        dto.setPoidsMoyenActuelKg(lot.getPoidsMoyenActuelKg());

        dto.setObservation(lot.getObservation());

        dto.setCreatedBy(lot.getCreatedBy());
        dto.setCreatedAt(lot.getCreatedAt());
        dto.setArchivedAt(lot.getArchivedAt());

        dto.setTauxMortalite(calculerTauxMortalite(lotId));
        dto.setGmqMoyen(calculerGMQMoyen(lotId));

        dto.setMouvements(getMouvements(lotId));
        dto.setPesees(getPesees(lotId));
        dto.setSuivisSanitaires(getSuivisSanitaires(lotId));
        dto.setVaccinations(getVaccinations(lotId));
        dto.setDistributions(getDistributions(lotId));

        return dto;
    }
}