package com.madaporc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final TypeMouvementLotRepository typeMouvementLotRepository;
    private final MouvementLotPorcRepository mouvementLotPorcRepository;
    private final PeseeLotRepository peseeLotRepository;
    private final SuiviSanitaireRepository suiviSanitaireRepository;
    private final VaccinationRepository vaccinationRepository;
    private final DistributionAlimentRepository distributionAlimentRepository;
    private final DetailVenteRepository detailVenteRepository;
    private final CycleProductionService cycleProductionService;

    public LotPorcService(LotPorcRepository lotPorcRepository, RaceRepository raceRepository,
            StatutLotRepository statutLotRepository,
            TypeMouvementLotRepository typeMouvementLotRepository,
            MouvementLotPorcRepository mouvementLotPorcRepository, PeseeLotRepository peseeLotRepository,
            SuiviSanitaireRepository suiviSanitaireRepository, VaccinationRepository vaccinationRepository,
            DistributionAlimentRepository distributionAlimentRepository, DetailVenteRepository detailVenteRepository,
            CycleProductionService cycleProductionService) {
        this.lotPorcRepository = lotPorcRepository;
        this.raceRepository = raceRepository;
        this.statutLotRepository = statutLotRepository;
        this.typeMouvementLotRepository = typeMouvementLotRepository;
        this.mouvementLotPorcRepository = mouvementLotPorcRepository;
        this.peseeLotRepository = peseeLotRepository;
        this.suiviSanitaireRepository = suiviSanitaireRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.distributionAlimentRepository = distributionAlimentRepository;
        this.detailVenteRepository = detailVenteRepository;
        this.cycleProductionService = cycleProductionService;
    }

    public List<LotPorc> rechercherLots(String code, Long raceId, Long statutId, LocalDate date) {
        return lotPorcRepository.findAll().stream()
                .filter(lot -> code == null || code.isBlank() || lot.getCodeLot() != null
                        && lot.getCodeLot().toLowerCase().contains(code.toLowerCase()))
                .filter(lot -> raceId == null || raceId.equals(lot.getRaceId()))
                .filter(lot -> statutId == null || statutId.equals(lot.getStatutLotId()))
                .filter(lot -> date == null || lot.getCreatedAt() != null && lot.getCreatedAt().toLocalDate().equals(date))
                .toList();
    }

    public Optional<LotPorc> findLot(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return lotPorcRepository.findById(id);
    }

    public String archiverLot(Long lotId) {
        if (lotId == null) {
            return "Lot introuvable.";
        }
        LotPorc lot = lotPorcRepository.findById(lotId).orElse(null);
        if (lot == null)
            return "Lot introuvable.";
        lot.setStatutLotId(3L);
        lot.setArchivedAt(LocalDateTime.now());
        lotPorcRepository.save(lot);
        return null;
    }

    public boolean verifierLotActif(Long lotId) {
        if (lotId == null) {
            return false;
        }
        return lotPorcRepository.findById(lotId).map(l -> l.getArchivedAt() == null).orElse(false);
    }

    public long countLots() {
        return lotPorcRepository.count();
    }

    public long countLotsActifs() {
        return lotPorcRepository.findAll().stream().filter(l -> l.getArchivedAt() == null).count();
    }

    public long countLotsEnAlerte() {
        return lotPorcRepository.findAll().stream()
                .filter(l -> l.getArchivedAt() == null)
                .filter(l -> l.getNombreActuel() != null && l.getNombreActuel() <= 5)
                .count();
    }

    public long countLotsNouveauxCeMois() {
        return lotPorcRepository.findAll().stream()
                .filter(l -> l.getCreatedAt() != null)
                .filter(l -> l.getCreatedAt().toLocalDate().getMonth() == LocalDate.now().getMonth())
                .filter(l -> l.getCreatedAt().toLocalDate().getYear() == LocalDate.now().getYear())
                .count();
    }

    public int totalNombreActuelActifs() {
        return nz(lotPorcRepository.sumNombreActuelActifs());
    }

    private int nz(Integer value) {
        return value == null ? 0 : value;
    }

    public void prepareLotListModel(Model model, String code, Long raceId, Long statutId, LocalDate date) {
        model.addAttribute("lots", rechercherLots(code, raceId, statutId, date));
        List<Race> races = raceRepository.findAll();
        List<StatutLot> statuts = statutLotRepository.findAll();
        Map<Long, String> raceLibelles = races.stream().collect(Collectors.toMap(Race::getId, Race::getLibelle));
        Map<Long, String> statutLibelles = statuts.stream().collect(Collectors.toMap(StatutLot::getId, StatutLot::getLibelle));

        model.addAttribute("races", races);
        model.addAttribute("statuts", statuts);
        model.addAttribute("raceLibelles", raceLibelles);
        model.addAttribute("statutLibelles", statutLibelles);
    }

    public Map<Long, String> buildRaceLibelles() {
        return raceRepository.findAll().stream()
                .collect(Collectors.toMap(Race::getId, Race::getLibelle));
    }

    public Map<Long, String> buildStatutLibelles() {
        return statutLotRepository.findAll().stream()
                .collect(Collectors.toMap(StatutLot::getId, StatutLot::getLibelle));
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
        lot = lotPorcRepository.save(lot);
        creerMouvementInitial(lot, utilisateurId);
        cycleProductionService.creerCycleDepuisLotSiAbsent(lot);
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
        cycleProductionService.synchroniserCycleAvecLot(lot);
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
        if (!"Naissance".equalsIgnoreCase(dto.getTypeEntree()) && !"Achat".equalsIgnoreCase(dto.getTypeEntree()))
            return "Type entrée obligatoire.";
        if (dto.getRaceId() == null)
            return "Race obligatoire.";
        if ("Naissance".equalsIgnoreCase(dto.getTypeEntree())) {
            if (dto.getNombreInitial() == null || dto.getNombreInitial() <= 0)
                return "Nombre initial invalide.";
            if (dto.getDateNaissanceEstimee() == null)
                return "Date naissance obligatoire.";
        }
        if ("Achat".equalsIgnoreCase(dto.getTypeEntree())) {
            if (dto.getNombreActuel() == null || dto.getNombreActuel() < 0)
                return "Nombre actuel invalide.";
            if (dto.getDateAchat() == null)
                return "Date achat obligatoire.";
        }
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
        lot.setStatutLotId(dto.getStatutLotId() == null ? 1L : dto.getStatutLotId());
        lot.setNombreMalesInitial(dto.getNombreMalesInitial());
        lot.setNombreFemellesInitial(dto.getNombreFemellesInitial());
        lot.setNombreInitial(dto.getNombreInitial());
        int totalMorts = nz(dto.getNombreMalesMorts()) + nz(dto.getNombreFemellesMortes());
        lot.setNombreMorts(totalMorts);
        lot.setNombreMalesMorts(dto.getNombreMalesMorts());
        lot.setNombreFemellesMortes(dto.getNombreFemellesMortes());
        lot.setNombreMalesActuel(dto.getNombreMalesActuel());
        lot.setNombreFemellesActuel(dto.getNombreFemellesActuel());
        if ("Achat".equalsIgnoreCase(dto.getTypeEntree())) {
            lot.setNombreActuel(dto.getNombreActuel());
        } else {
            int actuel = dto.getNombreActuel() != null ? dto.getNombreActuel()
                    : dto.getNombreInitial() == null ? 0 : Math.max(0, dto.getNombreInitial() - totalMorts);
            lot.setNombreActuel(actuel);
        }
        lot.setDateNaissanceEstimee(dto.getDateNaissanceEstimee());
        lot.setDateAchat(dto.getDateAchat());
        lot.setPrixAchatTotal(dto.getPrixAchatTotal());
        lot.setPoidsMoyenInitialKg(dto.getPoidsMoyenInitialKg());
        lot.setPoidsMoyenActuelKg(dto.getPoidsMoyenInitialKg());
        lot.setObservation(dto.getObservation());
        return lot;
    }

    private void creerMouvementInitial(LotPorc lot, Long utilisateurId) {
        MouvementLotPorc mouvement = new MouvementLotPorc();
        mouvement.setLotPorcId(lot.getId());
        mouvement.setTypeMouvementLotId(resolveTypeMouvementInitial(lot.getTypeEntree()));
        mouvement.setQuantite("Achat".equalsIgnoreCase(lot.getTypeEntree()) ? lot.getNombreActuel() : lot.getNombreInitial());
        mouvement.setQuantiteMale("Achat".equalsIgnoreCase(lot.getTypeEntree()) ? lot.getNombreMalesActuel() : lot.getNombreMalesInitial());
        mouvement.setQuantiteFemelle("Achat".equalsIgnoreCase(lot.getTypeEntree()) ? lot.getNombreFemellesActuel() : lot.getNombreFemellesInitial());
        mouvement.setDateMouvement(resolveDateMouvementInitial(lot));
        mouvement.setMotif("Création du lot " + lot.getCodeLot());
        mouvement.setCreatedBy(utilisateurId);
        mouvement.setCreatedAt(LocalDateTime.now());
        mouvementLotPorcRepository.save(mouvement);
    }

    private Long resolveTypeMouvementInitial(String typeEntree) {
        String libelle = "Achat".equalsIgnoreCase(typeEntree) ? "Achat" : "Naissance";
        return typeMouvementLotRepository.findByLibelleIgnoreCase(libelle)
                .map(TypeMouvementLot::getId)
                .orElse("Achat".equalsIgnoreCase(typeEntree) ? 2L : 1L);
    }

    private LocalDateTime resolveDateMouvementInitial(LotPorc lot) {
        if ("Achat".equalsIgnoreCase(lot.getTypeEntree()) && lot.getDateAchat() != null)
            return lot.getDateAchat().atStartOfDay();
        if (lot.getDateNaissanceEstimee() != null)
            return lot.getDateNaissanceEstimee().atStartOfDay();
        return LocalDateTime.now();
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
        dto.setNombreMorts(lot.getNombreMorts());
        dto.setNombreMalesMorts(lot.getNombreMalesMorts());
        dto.setNombreFemellesMortes(lot.getNombreFemellesMortes());
        dto.setNombreActuel(lot.getNombreActuel());
        dto.setNombreMalesActuel(lot.getNombreMalesActuel());
        dto.setNombreFemellesActuel(lot.getNombreFemellesActuel());
        dto.setDateNaissanceEstimee(lot.getDateNaissanceEstimee());
        dto.setDateAchat(lot.getDateAchat());
        dto.setPrixAchatTotal(lot.getPrixAchatTotal());
        dto.setPoidsMoyenInitialKg(lot.getPoidsMoyenInitialKg());
        dto.setObservation(lot.getObservation());
        return dto;
    }

    public LotDetailDTO getDetailLot(Long lotId) {
        LotDetailDTO dto = new LotDetailDTO();
        if (lotId != null) {
            lotPorcRepository.findById(lotId).ifPresent(lot -> {
                dto.setId(lot.getId());
                dto.setCodeLot(lot.getCodeLot());
                dto.setTypeEntree(lot.getTypeEntree());
                dto.setRaceId(lot.getRaceId());
                Long raceId = lot.getRaceId();
                Long statutLotId = lot.getStatutLotId();
                dto.setRaceLibelle(raceId != null ? raceRepository.findById(raceId).map(Race::getLibelle).orElse("-") : "-");
                dto.setStatutLotId(statutLotId);
                dto.setStatutLibelle(statutLotId != null ? statutLotRepository.findById(statutLotId).map(StatutLot::getLibelle).orElse("-") : "-");
                dto.setNombreInitial(lot.getNombreInitial());
                dto.setNombreActuel(lot.getNombreActuel());
                dto.setNombreMalesInitial(lot.getNombreMalesInitial());
                dto.setNombreFemellesInitial(lot.getNombreFemellesInitial());
                dto.setNombreMorts(lot.getNombreMorts());
                dto.setDateNaissanceEstimee(lot.getDateNaissanceEstimee());
                dto.setDateAchat(lot.getDateAchat());
                dto.setPrixAchatTotal(lot.getPrixAchatTotal());
                dto.setPoidsMoyenInitialKg(lot.getPoidsMoyenInitialKg());
                dto.setPoidsMoyenActuelKg(lot.getPoidsMoyenActuelKg());
                dto.setObservation(lot.getObservation());
            });
        }
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
        if (lotId == null) {
            return BigDecimal.ZERO;
        }
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
