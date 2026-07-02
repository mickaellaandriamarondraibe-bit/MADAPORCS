package com.madaporc.service;

<<<<<<< HEAD
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public void annulerVente(Long id) {
        Vente vente = chargerVente(id);
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

		for (DetailVente detail : vente.getLignes()) {
			if (detail.getLot() == null || detail.getQuantite() == null || detail.getQuantite() <= 0) {
				throw new IllegalArgumentException("Une ligne de vente est incomplète.");
			}

			mouvementService.diminuerEffectif(detail.getLot().getId(), detail.getQuantite());
		}

		vente.setStatut("VALIDEE");
		venteRepository.save(vente);
	}
}
=======
import com.madaporc.model.DetailVente;
import com.madaporc.model.LotPorc;
import com.madaporc.model.MouvementLotPorc;
import com.madaporc.model.Vente;
import com.madaporc.repository.DetailVenteRepository;
import com.madaporc.repository.VenteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.madaporc.dto.*;

@Service
public class VenteService {
    private final VenteRepository venteRepository;
    private final DetailVenteRepository detailVenteRepository;
    private final LotPorcService lotPorcService;
    private final MouvementLotService mouvementLotService;

    public VenteService(
            VenteRepository venteRepository,
            DetailVenteRepository detailVenteRepository,
            LotPorcService lotPorcService,
            MouvementLotService mouvementLotService) {

    this.venteRepository = venteRepository;
    this.detailVenteRepository = detailVenteRepository;
    this.lotPorcService = lotPorcService;
    this.mouvementLotService = mouvementLotService;
    }

    public List<Vente> findAll() {
        List<Vente> ventes = venteRepository.findAll();
        return ventes;
    }

    public Vente findById(Long id) {
        return venteRepository.findById(id).orElse(null);
    }

    public VenteDTO getForm(Long id) {
        Vente vente = findById(id);

        if (vente == null) {
            return null;
        }

        List<DetailVente> detailVente = getDetailsVente(vente.getId());
        if(detailVente.size() <= 0) {
            return null;
        }

        BigDecimal qteTotal = new BigDecimal(0);
        for(DetailVente dv : detailVente) {
            qteTotal.add(dv.getPoidsTotal());
        }

        VenteDTO venteDTO = new VenteDTO();
        venteDTO.setId(vente.getId());
        venteDTO.setPoidsTotal(qteTotal);
        venteDTO.setPrixUnitaire(detailVente.get(0).getPrixUnitaire());
        //I venteDTO.setClientId(vente.getClient().getId());
        venteDTO.setDateVente(vente.getDateVente());
        // venteDTO.setStatut(vente.getStatut());

        return venteDTO;
    }

    public String creerVente(VenteDTO venteDTO) {
        Vente vente = new Vente();
        vente.setDateVente(venteDTO.getDateVente());
        vente.setMontantTotal(calculerMontantTotal(venteDTO));
        if(venteDTO.getStatut() == null) {
            vente.setStatut("brouillon");
        } else {
            vente.setStatut(venteDTO.getStatut());
        }

        venteRepository.save(vente);
        return "redirect:/ventes";
    }

    public List<DetailVente> getDetailsVente(Long venteID) {
        return detailVenteRepository.findByVenteId(venteID);
    }

    public String validerVente(Long venteId) {
        VenteDTO venteDTO = getForm(venteId);

        if (venteDTO == null) {
            return "redirect:/ventes?error=venteNotFound";
        }

        LotDetailDTO detailLotDTO = lotPorcService.getDetailLot(venteDTO.getLotId());
        if(detailLotDTO == null) {
            return "redirect:/ventes?error=lotNotFound";
        }

        if(!mouvementLotService.verifierQuantiteDisponible(venteDTO.getLotId(), venteDTO.getQuantite())) {
            return "redirect:/ventes?error=quantiteInsuffisante";
        }

        venteDTO.setStatut("valide");
        creerVente(venteDTO);
        return "redirect:/ventes";
    }

    public String annulerVente(Long venteId) {
        VenteDTO venteDTO = getForm(venteId);

        if (venteDTO == null) {
            return "redirect:/ventes?error=venteNotFound";
        }

        venteDTO.setStatut("annule");
        creerVente(venteDTO);
        return "redirect:/ventes";
    }

    public BigDecimal calculerMontantTotal(VenteDTO venteDTO) {
        return venteDTO.getPrixUnitaire().multiply(new BigDecimal(venteDTO.getQuantite()));
    }

    public String validerDonneesVente(VenteDTO venteDTO) {
        if(venteDTO.getClientId() == null) {
            return "redirect:/ventes/form?error=clientIdManquant";
        }

        if(venteDTO.getLotId() == null) {
            return "redirect:/ventes/form?error=lotIdManquant";
        }

        if(venteDTO.getQuantite() == null || venteDTO.getQuantite() <= 0) {
            return "redirect:/ventes/form?error=quantiteInvalide";
        }

        if(venteDTO.getPrixUnitaire() == null || venteDTO.getPrixUnitaire().compareTo(BigDecimal.ZERO) <= 0) {
            return "redirect:/ventes/form?error=prixUnitaireInvalide";
        }

        return "redirect:/ventes/form";
    }

    public String creerMouvementVente(Long lotId, Integer quantite, Long venteId) {
        MouvementLotPorc mouvement = new MouvementLotPorc();
        LotPorc lot = lotPorcService.getLotById(lotId);
        Integer quantiteActuel = lot.getEffectifActuel();

        mouvement.setLot(lot);
        mouvement.setTypeMouvement("VENTE");
        mouvement.setQuantite(quantiteActuel - quantite);
        mouvement.setDateMouvement(LocalDate.now());
        mouvement.setObservation("Vente num" + venteId);

        mouvementLotService.saveMouvement(mouvement);
        return "redirect:/ventes";
    }
}
>>>>>>> 5e4d8bf (correction)
