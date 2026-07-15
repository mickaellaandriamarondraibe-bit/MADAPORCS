package com.madaporc.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

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
		verifierQuantitesDisponibles(dto);

		Vente vente;
		if (dto.getId() != null) {
			// Edition d'une vente existante (autorisee uniquement au statut BROUILLON).
			vente = chargerVente(dto.getId());
			if (!"BROUILLON".equalsIgnoreCase(vente.getStatut())) {
				throw new IllegalArgumentException("Seule une vente au statut BROUILLON peut être modifiée.");
			}
			// Pas de cascade/orphanRemoval sur la relation : on supprime les anciennes lignes a la main.
			detailVenteRepository.deleteAll(detailVenteRepository.findByVenteIdOrderByIdAsc(vente.getId()));
		} else {
			vente = new Vente();
			vente.setStatut("BROUILLON");
			vente.setCreatedAt(LocalDateTime.now());
		}
		appliquerDto(vente, dto);
		vente.setMontantTotal(BigDecimal.ZERO);
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

			if (ligneDto.getPrixUnitaire() == null
					|| ligneDto.getPrixUnitaire().compareTo(java.math.BigDecimal.ZERO) <= 0) {
				throw new IllegalArgumentException("Le prix unitaire doit être supérieur à zéro.");
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

				mouvementService.augmenterEffectif(detail.getLot().getId(), detail.getQuantite(),
						"Réintégration suite à l'annulation de la vente " + vente.getReference());
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

		for (DetailVente detail : vente.getLignes()) {
			if (detail.getLot() == null || detail.getQuantite() == null || detail.getQuantite() <= 0) {
				throw new IllegalArgumentException("Une ligne de vente est incomplète.");
			}

			mouvementService.diminuerEffectif(detail.getLot().getId(), detail.getQuantite());
		}

		vente.setStatut("VALIDEE");
		venteRepository.save(vente);
	}

	// Genere le recu PDF d'une vente (facture). Retourne null en cas d'erreur.
	@Transactional(readOnly = true)
	public byte[] genererRecuPdf(Long id) {
		Vente vente = chargerVente(id);
		NumberFormat nf = NumberFormat.getIntegerInstance(Locale.FRANCE);

		String statut = vente.getStatut();
		String statutLabel = "VALIDEE".equals(statut) ? "Validée"
				: "ANNULEE".equals(statut) ? "Annulée" : "Brouillon";
		com.madaporc.model.Client client = vente.getClient();
		String tel = client != null ? esc(client.getTelephone()) : "";
		String adr = client != null ? esc(client.getAdresse()) : "";

		StringBuilder html = new StringBuilder();
		html.append("<html><head><meta charset='UTF-8'/><style>")
			.append("@page{size:A5;margin:1.3cm;}")
			.append("body{font-family:sans-serif;color:#1b1f23;font-size:11px;}")
			.append(".box{border:1px solid #e6e9ee;border-radius:12px;padding:24px 28px;}")
			.append(".brand{text-align:center;color:#0b2545;font-weight:bold;font-size:15px;letter-spacing:1px;}")
			.append(".brand small{display:block;color:#6b7480;font-size:9px;font-weight:normal;letter-spacing:0;}")
			.append(".title{text-align:center;color:#13315c;letter-spacing:3px;font-size:17px;margin:14px 0 4px;}")
			.append(".lead{text-align:center;color:#6b7480;font-size:10px;margin:0 0 16px;}")
			.append(".label{text-transform:uppercase;font-size:8.5px;letter-spacing:.5px;color:#6b7480;font-weight:bold;}")
			.append(".sep{border:0;border-top:1px solid #e6e9ee;margin:14px 0;}")
			.append("table{width:100%;border-collapse:collapse;}")
			.append(".cols td{vertical-align:top;width:50%;padding:0;}")
			.append(".meta td{vertical-align:top;width:33%;padding:0;}")
			.append(".strong{font-weight:bold;font-size:12px;}")
			.append(".lines{margin-top:6px;}")
			.append(".lines th{text-align:left;font-size:8.5px;text-transform:uppercase;color:#6b7480;border-bottom:2px solid #e6e9ee;padding:6px 4px;}")
			.append(".lines td{padding:7px 4px;border-bottom:1px solid #f1f3f6;}")
			.append(".num{text-align:right;}")
			.append(".total td{border-top:2px solid #e6e9ee;font-weight:bold;color:#13315c;font-size:13px;padding-top:9px;}")
			.append(".paid{background:#effaf3;border:1px solid #d8f3e6;border-radius:8px;color:#245741;font-weight:bold;margin-top:10px;}")
			.append(".paid td{padding:9px 12px;}")
			.append(".foot{text-align:center;color:#9aa3af;font-size:9px;margin-top:18px;}")
			.append("</style></head><body><div class='box'>");

		html.append("<div class='brand'>MADAPORC<small>Gestion d'élevage</small></div>")
			.append("<div class='title'>REÇU DE VENTE</div>")
			.append("<p class='lead'>Ce reçu est délivré pour servir et valoir ce que de droit.</p>");

		html.append("<hr class='sep'/><table class='cols'><tr>")
			.append("<td><div class='label'>Vendeur</div><div class='strong'>MADAPORC</div>Gestion d'élevage porcin</td>")
			.append("<td><div class='label'>Client</div><div class='strong'>").append(esc(vente.getNomClient())).append("</div>");
		if (!tel.isEmpty()) html.append(tel).append("<br/>");
		if (!adr.isEmpty()) html.append(adr);
		html.append("</td></tr></table>");

		html.append("<hr class='sep'/><table class='meta'><tr>")
			.append("<td><div class='label'>Référence</div><b>").append(esc(vente.getReference())).append("</b></td>")
			.append("<td><div class='label'>Date de vente</div><b>").append(vente.getDateVente()).append("</b></td>")
			.append("<td><div class='label'>Statut</div><b>").append(statutLabel).append("</b></td>")
			.append("</tr></table>");

		html.append("<div class='label' style='margin-top:16px;'>Détail des lignes</div>")
			.append("<table class='lines'><tr><th>Lot</th><th class='num'>Qté</th>")
			.append("<th class='num'>Prix unitaire</th><th class='num'>Montant</th></tr>");
		if (vente.getLignes() != null) {
			for (DetailVente d : vente.getLignes()) {
				BigDecimal pu = d.getPrixUnitaire() != null ? d.getPrixUnitaire() : BigDecimal.ZERO;
				html.append("<tr><td><b>").append(esc(d.getCodeLot())).append("</b></td>")
					.append("<td class='num'>").append(d.getQuantite()).append("</td>")
					.append("<td class='num'>").append(nf.format(pu)).append(" Ar</td>")
					.append("<td class='num'>").append(nf.format(d.getTotal())).append(" Ar</td></tr>");
			}
		}
		html.append("<tr class='total'><td colspan='3' class='num'>TOTAL</td><td class='num'>")
			.append(nf.format(vente.getMontantTotal())).append(" Ar</td></tr></table>");

		if ("VALIDEE".equals(statut)) {
			html.append("<table class='paid'><tr><td>Montant payé</td><td class='num'>")
				.append(nf.format(vente.getMontantTotal())).append(" Ar</td></tr></table>");
		}

		html.append("<div class='foot'>Merci de votre confiance.</div></div></body></html>");

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PdfRendererBuilder builder = new PdfRendererBuilder();
			builder.useFastMode();
			builder.withHtmlContent(html.toString(), null);
			builder.toStream(out);
			builder.run();
			return out.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}

	private String esc(String s) {
		return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}
}
