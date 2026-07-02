package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

	public VenteService(VenteRepository venteRepository,
			DetailVenteRepository detailVenteRepository,
			ClientRepository clientRepository,
			LotPorcRepository lotPorcRepository) {
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
			dto.setLignes(creerLignesVides(3));
			return dto;
		}

		Vente vente = chargerVente(id);
		return toDto(vente);
	}

	public List<Client> listerClients() {
		return clientRepository.findAllByOrderByNomAsc();
	}

	public List<LotPorc> listerLots() {
		return lotPorcRepository.findAll().stream()
				.sorted(Comparator.comparing(LotPorc::getCodeLot))
				.collect(Collectors.toList());
	}

	@Transactional
	public Vente creerVente(VenteDTO dto) {
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

	public VenteDTO toDto(Vente vente) {
		VenteDTO dto = new VenteDTO();
		dto.setId(vente.getId());
		dto.setClientId(vente.getClient() != null ? vente.getClient().getId() : null);
		dto.setDateVente(vente.getDateVente());
		dto.setMontantTotal(vente.getMontantTotal());
		dto.setStatut(vente.getStatut());

		List<DetailVenteDTO> lignes = vente.getLignes() == null ? new ArrayList<>() : vente.getLignes().stream()
				.map(detail -> {
					DetailVenteDTO ligneDto = new DetailVenteDTO();
					ligneDto.setLotId(detail.getLot() != null ? detail.getLot().getId() : null);
					ligneDto.setQuantite(detail.getQuantite());
					ligneDto.setPrixUnitaire(detail.getPrixUnitaire());
					ligneDto.setPoidsTotal(detail.getPoidsTotal());
					return ligneDto;
				})
				.collect(Collectors.toList());

		if (lignes.isEmpty()) {
			lignes = creerLignesVides(3);
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
}
