package com.madaporc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.madaporc.DTO.LotDetailDTO;
import com.madaporc.DTO.LotPorcDTO;
import com.madaporc.model.*;
import com.madaporc.repository.*;

@Service
public class LotPorcService {

    private final LotPorcRepository lotPorcRepository;
    private final RaceRepository raceRepository;
    private final StatutLotRepository statutLotRepository;
    private final MouvementLotPorcRepository mouvementLotPorcRepository;
    private final PeseeLotRepository peseeLotRepository;
    private final SuiviSanitaireRepository suiviSanitaireRepository;
    private final VaccinationRepository vaccinationRepository;
    private final DistributionAlimentRepository distributionAlimentRepository;
    private final DetailVenteRepository detailVenteRepository;

    public LotPorcService(LotPorcRepository lotPorcRepository, RaceRepository raceRepository,
            StatutLotRepository statutLotRepository,
            MouvementLotPorcRepository mouvementLotPorcRepository, PeseeLotRepository peseeLotRepository,
            SuiviSanitaireRepository suiviSanitaireRepository, VaccinationRepository vaccinationRepository,
            DistributionAlimentRepository distributionAlimentRepository, DetailVenteRepository detailVenteRepository) {
        this.lotPorcRepository = lotPorcRepository;
        this.raceRepository = raceRepository;
        this.statutLotRepository = statutLotRepository;
        this.mouvementLotPorcRepository = mouvementLotPorcRepository;
        this.peseeLotRepository = peseeLotRepository;
        this.suiviSanitaireRepository = suiviSanitaireRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.distributionAlimentRepository = distributionAlimentRepository;
        this.detailVenteRepository = detailVenteRepository;
    }

    public List<LotPorc> rechercherLots(String code, Long raceId, Long statutId) {
        if (raceId != null && statutId != null)
            return lotPorcRepository.findByRaceIdAndStatutLotId(raceId, statutId);
        if (code != null && !code.isBlank())
            return lotPorcRepository.findByCodeLotContainingIgnoreCase(code);
        if (statutId != null)
            return lotPorcRepository.findByStatutLotId(statutId);
        return lotPorcRepository.findAll();
    }

    public Optional<LotPorc> findLot(Long id) {
        return lotPorcRepository.findById(id);
    }

    public String archiverLot(Long lotId) {
        LotPorc lot = lotPorcRepository.findById(lotId).orElse(null);
        if (lot == null)
            return "Lot introuvable.";
        lot.setStatutLotId(3L);
        lot.setArchivedAt(LocalDateTime.now());
        lotPorcRepository.save(lot);
        return null;
    }

    public boolean verifierLotActif(Long lotId) {
        return lotPorcRepository.findById(lotId).map(l -> l.getArchivedAt() == null).orElse(false);
    }

    public void prepareLotListModel(Model model, String code, Long raceId, Long statutId) {
        model.addAttribute("lots", rechercherLots(code, raceId, statutId));
        model.addAttribute("races", raceRepository.findAll());
        model.addAttribute("statuts", statutLotRepository.findAll());
    }

    public void prepareLotFormModel(Model model, Long id, String typeEntree) {
        LotPorcDTO dto = id == null ? new LotPorcDTO()
                : lotPorcRepository.findById(id).map(this::convertToDTO).orElse(new LotPorcDTO());
        if (dto.getTypeEntree() == null)
            dto.setTypeEntree(typeEntree);
        model.addAttribute("lot", dto);
        model.addAttribute("races", raceRepository.findAll());
        model.addAttribute("statuts", statutLotRepository.findAll());
    }

    public String creer(LotPorcDTO dto, Long utilisateurId) {
        String erreur = validerDonneesLot(dto);
        if (erreur != null)
            return erreur;
        erreur = verifierCodeUnique(dto.getCodeLot(), null);
        if (erreur != null)
            return erreur;
        LotPorc lot = convertirDtoVersEntity(dto);
        lot.setCreatedBy(utilisateurId);
        lot.setCreatedAt(LocalDateTime.now());
        lotPorcRepository.save(lot);
        return null;
    }

    public String modifier(Long id, LotPorcDTO dto) {
        String erreur = validerDonneesLot(dto);
        if (erreur != null)
            return erreur;
        erreur = verifierCodeUnique(dto.getCodeLot(), id);
        if (erreur != null)
            return erreur;
        LotPorc lot = convertirDtoVersEntity(dto);
        lot.setId(id);
        lotPorcRepository.save(lot);
        return null;
    }

    public String verifierCodeUnique(String codeLot, Long idActuel) {
        Optional<LotPorc> optional = lotPorcRepository.findByCodeLot(codeLot);
        if (optional.isEmpty())
            return null;
        if (idActuel != null && optional.get().getId().equals(idActuel))
            return null;
        return "Code lot déjà utilisé.";
    }

    public String validerDonneesLot(LotPorcDTO dto) {
        if (dto == null)
            return "Lot obligatoire.";
        if (dto.getCodeLot() == null || dto.getCodeLot().isBlank())
            return "Code lot obligatoire.";
        if (dto.getRaceId() == null)
            return "Race obligatoire.";
        if (dto.getNombreInitial() == null || dto.getNombreInitial() <= 0)
            return "Nombre initial invalide.";
        if (dto.getNombreActuel() == null || dto.getNombreActuel() < 0)
            return "Nombre actuel invalide.";
        if ("Achat".equalsIgnoreCase(dto.getTypeEntree()) && dto.getDateAchat() == null)
            return "Date achat obligatoire.";
        if ("Naissance".equalsIgnoreCase(dto.getTypeEntree()) && dto.getDateNaissanceEstimee() == null)
            return "Date naissance obligatoire.";
        if (dto.getDateAchat() != null && dto.getDateAchat().isAfter(LocalDate.now()))
            return "Date achat future interdite.";
        if (dto.getDateNaissanceEstimee() != null && dto.getDateNaissanceEstimee().isAfter(LocalDate.now()))
            return "Date naissance future interdite.";
        return null;
    }

    public LotPorc convertirDtoVersEntity(LotPorcDTO dto) {
        LotPorc lot = new LotPorc();
        lot.setId(dto.getId());
        lot.setTypeEntree(dto.getTypeEntree());
        lot.setCodeLot(dto.getCodeLot());
        lot.setRaceId(dto.getRaceId());
        lot.setStatutLotId(dto.getStatutLotId());
        lot.setNombreMalesInitial(dto.getNombreMalesInitial());
        lot.setNombreFemellesInitial(dto.getNombreFemellesInitial());
        lot.setNombreInitial(dto.getNombreInitial());
        lot.setNombreActuel(dto.getNombreActuel());
        lot.setDateNaissanceEstimee(dto.getDateNaissanceEstimee());
        lot.setDateAchat(dto.getDateAchat());
        lot.setPrixAchatTotal(dto.getPrixAchatTotal());
        lot.setPoidsMoyenInitialKg(dto.getPoidsMoyenInitialKg());
        lot.setPoidsMoyenActuelKg(dto.getPoidsMoyenInitialKg());
        lot.setObservation(dto.getObservation());
        return lot;
    }

    public LotPorcDTO convertToDTO(LotPorc lot) {
        LotPorcDTO dto = new LotPorcDTO();
        dto.setId(lot.getId());
        dto.setTypeEntree(lot.getTypeEntree());
        dto.setCodeLot(lot.getCodeLot());
        dto.setRaceId(lot.getRaceId());
        dto.setStatutLotId(lot.getStatutLotId());
        dto.setNombreMalesInitial(lot.getNombreMalesInitial());
        dto.setNombreFemellesInitial(lot.getNombreFemellesInitial());
        dto.setNombreInitial(lot.getNombreInitial());
        dto.setNombreActuel(lot.getNombreActuel());
        dto.setDateNaissanceEstimee(lot.getDateNaissanceEstimee());
        dto.setDateAchat(lot.getDateAchat());
        dto.setPrixAchatTotal(lot.getPrixAchatTotal());
        dto.setPoidsMoyenInitialKg(lot.getPoidsMoyenInitialKg());
        dto.setObservation(lot.getObservation());
        return dto;
    }

    public LotDetailDTO getDetailLot(Long lotId) {
        LotDetailDTO dto = new LotDetailDTO();
        lotPorcRepository.findById(lotId).ifPresent(lot -> {
            dto.setId(lot.getId());
            dto.setCodeLot(lot.getCodeLot());
            dto.setNombreActuel(lot.getNombreActuel());
        });
        dto.setTauxMortalite(calculerTauxMortalite(lotId));
        dto.setGmqMoyen(calculerGMQMoyen(lotId));
        return dto;
    }

    public List<MouvementLotPorc> getMouvements(Long lotId) {
        return mouvementLotPorcRepository.findByLotPorcIdOrderByDateMouvementDesc(lotId);
    }

    public List<PeseeLot> getPesees(Long lotId) {
        return peseeLotRepository.findByLotPorcIdOrderByDatePeseeAsc(lotId);
    }

    public List<SuiviSanitaire> getSuivisSanitaires(Long lotId) {
        return suiviSanitaireRepository.findByLotPorcId(lotId);
    }

    public List<Vaccination> getVaccinations(Long lotId) {
        return vaccinationRepository.findByLotPorcId(lotId);
    }

    public List<DistributionAliment> getDistributions(Long lotId) {
        return distributionAlimentRepository.findByLotPorcId(lotId);
    }

    public List<DetailVente> getVentes(Long lotId) {
        return detailVenteRepository.findByLotPorcId(lotId);
    }

    public BigDecimal calculerTauxMortalite(Long lotId) {
        LotPorc lot = lotPorcRepository.findById(lotId).orElse(null);
        if (lot == null || lot.getNombreInitial() == null || lot.getNombreInitial() == 0)
            return BigDecimal.ZERO;
        int morts = lot.getNombreMorts() == null ? 0 : lot.getNombreMorts();
        return BigDecimal.valueOf(morts).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(lot.getNombreInitial()), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculerGMQMoyen(Long lotId) {
        return BigDecimal.ZERO;
    }
}
