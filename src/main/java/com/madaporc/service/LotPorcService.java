package com.madaporc.service;

import com.madaporc.DTO.LotDetailDTO;
import com.madaporc.DTO.LotPorcDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.repository.LotPorcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LotPorcService {

    @Autowired
    private LotPorcRepository lotPorcRepository;

    public List<LotPorc> rechercherLots(String code, Long raceId, Long statutId) {
        if (code != null && !code.isEmpty() && raceId != null && statutId != null) {
            List<LotPorc> resultats = lotPorcRepository.findByArchivedAtIsNullAndCodeLotContainingIgnoreCase(code);
            return resultats.stream()
                    .filter(lot -> lot.getRaceId().equals(raceId) && lot.getStatutLotId().equals(statutId))
                    .toList();
        } else if (code != null && !code.isEmpty()) {
            return lotPorcRepository.findByArchivedAtIsNullAndCodeLotContainingIgnoreCase(code);
        } else if (raceId != null && statutId != null) {
            return lotPorcRepository.findByArchivedAtIsNullAndRaceIdAndStatutLotId(raceId, statutId);
        } else {
            return lotPorcRepository.findByArchivedAtIsNull();
        }
    }

    public void prepareLotListModel(Model model, String code, Long raceId, Long statutId) {
        List<LotPorc> lots = rechercherLots(code, raceId, statutId);
        model.addAttribute("lots", lots);
        model.addAttribute("filtreCode", code);
        model.addAttribute("filtreRaceId", raceId);
        model.addAttribute("filtreStatutId", statutId);
    }

    public void prepareLotFormModel(Model model, Long id, String typeEntree) {
        if (id != null) {
            Optional<LotPorc> lotOptional = lotPorcRepository.findById(id);
            if (lotOptional.isPresent()) {
                LotPorc lot = lotOptional.get();
                LotPorcDTO dto = convertirEntityVersDto(lot);
                model.addAttribute("lotPorcDTO", dto);
                model.addAttribute("mode", "modification");
            }
        } else {
            LotPorcDTO dto = new LotPorcDTO();
            if (typeEntree != null) {
                dto.setTypeEntree(typeEntree);
            }
            model.addAttribute("lotPorcDTO", dto);
            model.addAttribute("mode", "creation");
        }
    }

    public String creer(LotPorcDTO dto, Long utilisateurId) {
        String validation = validerDonneesLot(dto);
        if (!validation.equals("valide")) {
            return validation;
        }

        if (lotPorcRepository.existsByCodeLot(dto.getCodeLot())) {
            return "Le code lot existe déjà";
        }

        LotPorc lot = convertirDtoVersEntity(dto);
        lot.setCreatedBy(utilisateurId);
        lot.setCreatedAt(LocalDateTime.now());
        if (lot.getNombreMorts() == null) {
            lot.setNombreMorts(0);
        }

        lotPorcRepository.save(lot);
        return "Lot créé avec succès";
    }

    public String modifier(Long id, LotPorcDTO dto) {
        Optional<LotPorc> lotOptional = lotPorcRepository.findById(id);
        if (!lotOptional.isPresent()) {
            return "Lot non trouvé";
        }

        String validation = validerDonneesLot(dto);
        if (!validation.equals("valide")) {
            return validation;
        }

        String codeVerification = verifierCodeUnique(dto.getCodeLot(), id);
        if (!codeVerification.equals("valide")) {
            return codeVerification;
        }

        LotPorc lot = lotOptional.get();
        lot.setCodeLot(dto.getCodeLot());
        lot.setTypeEntree(dto.getTypeEntree());
        lot.setRaceId(dto.getRaceId());
        lot.setStatutLotId(dto.getStatutLotId());
        lot.setNombreInitial(dto.getNombreInitial());
        lot.setNombreActuel(dto.getNombreActuel());
        lot.setNombreMalesInitial(dto.getNombreMalesInitial());
        lot.setNombreFellesInitial(dto.getNombreFellesInitial());
        lot.setNombreMalesActuel(dto.getNombreMalesActuel());
        lot.setNombreFellesActuel(dto.getNombreFellesActuel());
        lot.setNombreMorts(dto.getNombreMorts());
        lot.setDateNaissanceEstimee(dto.getDateNaissanceEstimee());
        lot.setDateAchat(dto.getDateAchat());
        lot.setPrixAchatTotal(dto.getPrixAchatTotal());
        lot.setPoidsMoyenInitialKg(dto.getPoidsMoyenInitialKg());
        lot.setPoidsMoyenActuelKg(dto.getPoidsMoyenActuelKg());
        lot.setObservation(dto.getObservation());

        lotPorcRepository.save(lot);
        return "Lot modifié avec succès";
    }

    public String archiverLot(Long lotId) {
        Optional<LotPorc> lotOptional = lotPorcRepository.findById(lotId);
        if (lotOptional.isPresent()) {
            LotPorc lot = lotOptional.get();
            lot.setArchivedAt(LocalDateTime.now());
            lotPorcRepository.save(lot);
            return "Lot archivé avec succès";
        }
        return "Lot non trouvé";
    }

    public Optional<LotPorc> findLot(Long id) {
        return lotPorcRepository.findById(id);
    }

    public String verifierCodeUnique(String codeLot, Long idActuel) {
        List<LotPorc> existants = lotPorcRepository.findByCodeLotContainingIgnoreCase(codeLot);
        Optional<LotPorc> existing = existants.stream()
                .filter(lot -> !lot.getId().equals(idActuel))
                .findFirst();

        if (existing.isPresent()) {
            return "Le code lot existe déjà pour un autre lot";
        }
        return "valide";
    }

    public String validerDonneesLot(LotPorcDTO dto) {
        if (dto.getCodeLot() == null || dto.getCodeLot().trim().isEmpty()) {
            return "Le code lot est obligatoire";
        }

        if (dto.getNombreInitial() == null || dto.getNombreInitial() <= 0) {
            return "Le nombre initial doit être supérieur à 0";
        }

        if (dto.getNombreActuel() == null || dto.getNombreActuel() < 0) {
            return "Le nombre actuel ne peut pas être négatif";
        }

        if (dto.getTypeEntree() == null || dto.getTypeEntree().trim().isEmpty()) {
            return "Le type d'entrée est obligatoire";
        }

        if (!dto.getTypeEntree().equals("Naissance") && !dto.getTypeEntree().equals("Achat")) {
            return "Le type d'entrée doit être 'Naissance' ou 'Achat'";
        }

        if ("Achat".equals(dto.getTypeEntree()) && dto.getDateAchat() == null) {
            return "La date d'achat est obligatoire pour un achat";
        }

        if ("Naissance".equals(dto.getTypeEntree()) && dto.getDateNaissanceEstimee() == null) {
            return "La date de naissance estimée est obligatoire pour une naissance";
        }

        if (dto.getRaceId() == null) {
            return "La race est obligatoire";
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
        lot.setNombreMalesActuel(dto.getNombreMalesActuel());
        lot.setNombreFellesActuel(dto.getNombreFellesActuel());
        lot.setNombreMorts(dto.getNombreMorts() != null ? dto.getNombreMorts() : 0);
        lot.setDateNaissanceEstimee(dto.getDateNaissanceEstimee());
        lot.setDateAchat(dto.getDateAchat());
        lot.setPrixAchatTotal(dto.getPrixAchatTotal());
        lot.setPoidsMoyenInitialKg(dto.getPoidsMoyenInitialKg());
        lot.setPoidsMoyenActuelKg(dto.getPoidsMoyenActuelKg());
        lot.setObservation(dto.getObservation());
        return lot;
    }

    public boolean verifierLotActif(Long lotId) {
        Optional<LotPorc> lotOptional = lotPorcRepository.findById(lotId);
        return lotOptional.isPresent() && lotOptional.get().getArchivedAt() == null;
    }

    public LotDetailDTO getDetailLot(Long lotId) {
        Optional<LotPorc> lotOptional = lotPorcRepository.findById(lotId);
        if (!lotOptional.isPresent()) {
            return null;
        }

        LotPorc lot = lotOptional.get();
        LotDetailDTO detail = new LotDetailDTO();
        detail.setId(lot.getId());
        detail.setCodeLot(lot.getCodeLot());
        detail.setTypeEntree(lot.getTypeEntree());
        detail.setRaceId(lot.getRaceId());
        detail.setStatutLotId(lot.getStatutLotId());
        detail.setNombreInitial(lot.getNombreInitial());
        detail.setNombreActuel(lot.getNombreActuel());
        detail.setNombreMalesInitial(lot.getNombreMalesInitial());
        detail.setNombreFellesInitial(lot.getNombreFellesInitial());
        detail.setNombreMalesActuel(lot.getNombreMalesActuel());
        detail.setNombreFellesActuel(lot.getNombreFellesActuel());
        detail.setNombreMorts(lot.getNombreMorts());
        detail.setDateNaissanceEstimee(lot.getDateNaissanceEstimee());
        detail.setDateAchat(lot.getDateAchat());
        detail.setPrixAchatTotal(lot.getPrixAchatTotal());
        detail.setPoidsMoyenInitialKg(lot.getPoidsMoyenInitialKg());
        detail.setPoidsMoyenActuelKg(lot.getPoidsMoyenActuelKg());
        detail.setObservation(lot.getObservation());
        detail.setCreatedBy(lot.getCreatedBy());
        detail.setCreatedAt(lot.getCreatedAt());
        detail.setArchivedAt(lot.getArchivedAt());
        detail.setTauxMortalite(calculerTauxMortalite(lotId));
        detail.setGmqMoyen(calculerGMQMoyen(lotId));
        detail.setMouvements(getMouvements(lotId));
        detail.setPesees(getPesees(lotId));
        detail.setSuivisSanitaires(getSuivisSanitaires(lotId));
        detail.setVaccinations(getVaccinations(lotId));
        detail.setDistributions(getDistributions(lotId));
        detail.setVentes(getVentes(lotId));

        return detail;
    }

    public List<Object> getMouvements(Long lotId) {
        return List.of();
    }

    public List<Object> getPesees(Long lotId) {
        return List.of();
    }

    public List<Object> getSuivisSanitaires(Long lotId) {
        return List.of();
    }

    public List<Object> getVaccinations(Long lotId) {
        return List.of();
    }

    public List<Object> getDistributions(Long lotId) {
        return List.of();
    }

    public List<Object> getVentes(Long lotId) {
        return List.of();
    }

    public BigDecimal calculerTauxMortalite(Long lotId) {
        Optional<LotPorc> lotOptional = lotPorcRepository.findById(lotId);
        if (!lotOptional.isPresent()) {
            return BigDecimal.ZERO;
        }

        LotPorc lot = lotOptional.get();
        if (lot.getNombreInitial() == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal nombreMorts = new BigDecimal(lot.getNombreMorts() != null ? lot.getNombreMorts() : 0);
        BigDecimal nombreInitial = new BigDecimal(lot.getNombreInitial());
        return nombreMorts.divide(nombreInitial, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal(100));
    }

    public BigDecimal calculerGMQMoyen(Long lotId) {
        Optional<LotPorc> lotOptional = lotPorcRepository.findById(lotId);
        if (!lotOptional.isPresent()) {
            return BigDecimal.ZERO;
        }

        LotPorc lot = lotOptional.get();
        if (lot.getPoidsMoyenInitialKg() == null || lot.getPoidsMoyenActuelKg() == null) {
            return BigDecimal.ZERO;
        }

        return lot.getPoidsMoyenActuelKg().subtract(lot.getPoidsMoyenInitialKg());
    }

    public LotPorcDTO convertirEntityVersDto(LotPorc lot) {
        LotPorcDTO dto = new LotPorcDTO();
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
        return dto;
    }
}