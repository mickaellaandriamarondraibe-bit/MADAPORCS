package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madaporc.dto.DetailVenteDTO;
import com.madaporc.dto.VenteDTO;
import com.madaporc.model.Client;
import com.madaporc.model.DetailVente;
import com.madaporc.model.LotPorc;
import com.madaporc.model.Vente;
import com.madaporc.repository.ClientRepository;
import com.madaporc.repository.DetailVenteRepository;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.VenteRepository;

@Service
public class VenteService {

	private final VenteRepository venteRepository;
	private final DetailVenteRepository detailVenteRepository;
	private final ClientRepository clientRepository;
	private final LotPorcRepository lotPorcRepository;
	private final MouvementLotService mouvementService;

	public VenteService(VenteRepository venteRepository,
			DetailVenteRepository detailVenteRepository,
			ClientRepository clientRepository,
			LotPorcRepository lotPorcRepository, 
            MouvementLotService mouvementService) {

        this.mouvementService = mouvementService;
		this.venteRepository = venteRepository;
		this.detailVenteRepository = detailVenteRepository;
		this.clientRepository = clientRepository;
		this.lotPorcRepository = lotPorcRepository;
	}

	public List<Vente> listerVentes() {
		return venteRepository.findAllByOrderByCreatedAtDesc();
	}

	public Vente chargerVente(Long id) {
		return venteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Vente introuvable"));
	}

	public VenteDTO preparerFormulaire(Long id) {
		if (id == null) {
			VenteDTO dto = new VenteDTO();
			dto.setDateVente(LocalDate.now());
			dto.setLignes(creerLignesVides(5));
			return dto;
		}

		Vente vente = chargerVente(id);
		return toDto(vente);
	}

	public List<Client> listerClients() {
		return clientRepository.findAllByOrderByNomAsc();
	}

	public List<LotPorc> listerLots() {
        List<LotPorc> lots = lotPorcRepository.findAll();

        for (int i = 0; i < lots.size() - 1; i++) {
            for (int j = i + 1; j < lots.size(); j++) {
                if (lots.get(i).getCodeLot().compareTo(lots.get(j).getCodeLot()) > 0) {
                    LotPorc temp = lots.get(i);
                    lots.set(i, lots.get(j));
                    lots.set(j, temp);
                }
            }
        }

        return lots;
    }

	@Transactional
	public Vente creerVente(VenteDTO dto) {
		verifierQuantitesDisponibles(dto);

		Vente vente = new Vente();
		appliquerDto(vente, dto);
		vente.setStatut("BROUILLON");
		vente.setMontantTotal(BigDecimal.ZERO);
		vente.setCreatedAt(LocalDateTime.now());
		vente.setUpdatedAt(LocalDateTime.now());

		Vente venteEnregistree = venteRepository.save(vente);

		List<DetailVente> details = new ArrayList<>();
		BigDecimal total = BigDecimal.ZERO;

		if (dto.getLignes() != null) {
			for (DetailVenteDTO ligneDto : dto.getLignes()) {
				if (ligneDto == null || ligneDto.getLotId() == null || ligneDto.getQuantite() == null
						|| ligneDto.getPrixUnitaire() == null) {
					continue;
				}

				if (ligneDto.getQuantite() <= 0) {
					throw new IllegalArgumentException("La quantité doit être supérieure à zéro");
				}

				if (ligneDto.getPrixUnitaire().compareTo(BigDecimal.ZERO) < 0) {
					throw new IllegalArgumentException("Le prix unitaire ne peut pas être négatif");
				}

				if(!quantiteDisponible(lotPorcRepository.findById(ligneDto.getLotId())
						.orElseThrow(() -> new IllegalArgumentException("Lot introuvable")), ligneDto.getQuantite())) {
					throw new IllegalArgumentException("La quantité demandée pour le lot " + ligneDto.getLotId() + " dépasse l'effectif disponible.");
				}

				if(ligneDto.getPrixUnitaire().compareTo(BigDecimal.ZERO) <= 0) {
					throw new IllegalArgumentException("Le prix unitaire doit etre superieur à zéro");
				}

				LotPorc lot = lotPorcRepository.findById(ligneDto.getLotId())
						.orElseThrow(() -> new IllegalArgumentException("Lot introuvable"));

				DetailVente detail = new DetailVente();
				detail.setVente(venteEnregistree);
				detail.setLot(lot);
				detail.setQuantite(ligneDto.getQuantite());
				detail.setPrixUnitaire(ligneDto.getPrixUnitaire());
				BigDecimal montant = ligneDto.getPrixUnitaire().multiply(BigDecimal.valueOf(ligneDto.getQuantite()));
				detail.setMontant(montant);
				detail.setPoidsTotal(ligneDto.getPoidsTotal());
				details.add(detail);
				total = total.add(montant);
			}
		}

		if (!details.isEmpty()) {
			detailVenteRepository.saveAll(details);
		}

		venteEnregistree.setMontantTotal(total);
		venteEnregistree.setLignes(details);
		venteRepository.save(venteEnregistree);

		return chargerVente(venteEnregistree.getId());
	}

	private void verifierQuantitesDisponibles(VenteDTO dto) {
		if (dto == null || dto.getLignes() == null) {
			return;
		}

		Map<Long, Integer> quantitesParLot = new HashMap<>();
		for (DetailVenteDTO ligneDto : dto.getLignes()) {
			if (ligneDto == null || ligneDto.getLotId() == null || ligneDto.getQuantite() == null) {
				continue;
			}

			if (ligneDto.getQuantite() <= 0) {
				throw new IllegalArgumentException("La quantité doit être supérieure à zéro");
			}

			quantitesParLot.merge(ligneDto.getLotId(), ligneDto.getQuantite(), Integer::sum);
		}

		for (Map.Entry<Long, Integer> entry : quantitesParLot.entrySet()) {
			LotPorc lot = lotPorcRepository.findById(entry.getKey())
					.orElseThrow(() -> new IllegalArgumentException("Lot introuvable"));

			if (entry.getValue() > lot.getEffectifActuel()) {
				throw new IllegalArgumentException(
						"La quantité demandée pour le lot " + lot.getCodeLot() + " dépasse l'effectif disponible.");
			}
		}
	}

	private void verifierQuantitesDisponibles(Vente vente) {
		Map<Long, Integer> quantitesParLot = new HashMap<>();
		Map<Long, LotPorc> lotsParId = new HashMap<>();

		for (DetailVente detail : vente.getLignes()) {
			if (detail.getLot() == null || detail.getQuantite() == null || detail.getQuantite() <= 0) {
				throw new IllegalArgumentException("Une ligne de vente est incomplète.");
			}

			Long lotId = detail.getLot().getId();
			quantitesParLot.merge(lotId, detail.getQuantite(), Integer::sum);
			lotsParId.putIfAbsent(lotId, detail.getLot());
		}

		for (Map.Entry<Long, Integer> entry : quantitesParLot.entrySet()) {
			LotPorc lot = lotsParId.get(entry.getKey());

			if (!quantiteDisponible(lot, entry.getValue())) {
				throw new IllegalArgumentException(
						"La quantité demandée pour le lot " + lot.getCodeLot() + " dépasse l'effectif disponible.");
			}
		}
	}

    public boolean quantiteDisponible(LotPorc lot, Integer quantiteDemandee) {
        if (lot == null || quantiteDemandee == null) {
            return false;
        }
        return quantiteDemandee <= lot.getEffectifActuel();
    }

	public VenteDTO toDto(Vente vente) {
		VenteDTO dto = new VenteDTO();
		dto.setId(vente.getId());
		dto.setClientId(vente.getClient() != null ? vente.getClient().getId() : null);
		dto.setDateVente(vente.getDateVente());
		dto.setMontantTotal(vente.getMontantTotal());
		dto.setStatut(vente.getStatut());

		List<DetailVenteDTO> lignes = new ArrayList<>();

        if (vente.getLignes() != null) {
            for (DetailVente detail : vente.getLignes()) {
                DetailVenteDTO ligneDto = new DetailVenteDTO();

                if (detail.getLot() != null) {
                    ligneDto.setLotId(detail.getLot().getId());
                }

                ligneDto.setQuantite(detail.getQuantite());
                ligneDto.setPrixUnitaire(detail.getPrixUnitaire());
                ligneDto.setPoidsTotal(detail.getPoidsTotal());

                lignes.add(ligneDto);
            }
        }

		// ---
		if (lignes.isEmpty()) {
			lignes = creerLignesVides(5);
		}

		dto.setLignes(lignes);
		return dto;
	}

	private void appliquerDto(Vente vente, VenteDTO dto) {
		if (dto.getClientId() != null) {
			Client client = clientRepository.findById(dto.getClientId())
					.orElseThrow(() -> new IllegalArgumentException("Client introuvable"));
			vente.setClient(client);
		} else {
			vente.setClient(null);
		}

		vente.setDateVente(dto.getDateVente() != null ? dto.getDateVente() : LocalDate.now());
	}

	private List<DetailVenteDTO> creerLignesVides(int nombre) {
		List<DetailVenteDTO> lignes = new ArrayList<>();
		for (int i = 0; i < nombre; i++) {
			lignes.add(new DetailVenteDTO());
		}
		return lignes;
	}

	@Transactional
	public void annulerVente(Long id) {
		Vente vente = chargerVente(id);

		if ("ANNULEE".equalsIgnoreCase(vente.getStatut())) {
			return;
		}

		if ("VALIDEE".equalsIgnoreCase(vente.getStatut())) {
			if (vente.getLignes() == null || vente.getLignes().isEmpty()) {
				throw new IllegalArgumentException("Impossible d'annuler une vente validée sans lignes.");
			}

			for (DetailVente detail : vente.getLignes()) {
				if (detail.getLot() == null || detail.getQuantite() == null || detail.getQuantite() <= 0) {
					throw new IllegalArgumentException("Une ligne de vente est incomplète.");
				}

				mouvementService.augmenterEffectif(detail.getLot().getId(), detail.getQuantite());
			}
		}

		vente.setStatut("ANNULEE");
		venteRepository.save(vente);
	}

	@Transactional
	public void validerVente(Long id) {
		Vente vente = chargerVente(id);

		if ("VALIDEE".equalsIgnoreCase(vente.getStatut())) {
			return;
		}

		if ("ANNULEE".equalsIgnoreCase(vente.getStatut())) {
			throw new IllegalArgumentException("Impossible de valider une vente annulée.");
		}

		if (vente.getLignes() == null || vente.getLignes().isEmpty()) {
			throw new IllegalArgumentException("Aucune ligne de vente à valider.");
		}

		verifierQuantitesDisponibles(vente);

		for (DetailVente detail : vente.getLignes()) {
			mouvementService.diminuerEffectif(detail.getLot().getId(), detail.getQuantite());
		}

		vente.setStatut("VALIDEE");
		venteRepository.save(vente);
	}
}
