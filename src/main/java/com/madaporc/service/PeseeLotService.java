package com.madaporc.service;

import com.madaporc.dto.PeseeLotDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.PeseeLot;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.PeseeLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PeseeLotService {

    private final PeseeLotRepository peseeLotRepository;
    private final LotPorcRepository lotPorcRepository;

    public List<PeseeLot> getPeseesByLot(Long lotId) {
        return peseeLotRepository.findByLotIdOrderByDatePeseeAsc(lotId);
    }

    public String enregistrerPesee(PeseeLotDTO dto) {
        if (dto == null || dto.getLotId() == null) {
            return "Lot invalide.";
        }
        if (dto.getDatePesee() == null) {
            return "Date de pesée obligatoire.";
        }
        if (dto.getPoidsMoyen() == null) {
            return "Poids moyen obligatoire.";
        }

        BigDecimal poids = dto.getPoidsMoyen();
        if (poids.compareTo(BigDecimal.ZERO) <= 0) {
            return "Le poids moyen doit être supérieur à 0.";
        }
        if (dto.getDatePesee().isAfter(LocalDate.now())) {
            return "La date de pesée ne peut pas être dans le futur.";
        }

        LotPorc lot = lotPorcRepository.findById(dto.getLotId())
                .orElse(null);
        if (lot == null) {
            return "Lot introuvable.";
        }

        PeseeLot pesee = new PeseeLot();
        pesee.setLot(lot);
        pesee.setDatePesee(dto.getDatePesee());
        pesee.setPoidsMoyen(poids);
        pesee.setObservation(dto.getObservation());

        peseeLotRepository.save(pesee);
        return null; // succès
    }

    public BigDecimal calculerEvolutionPoids(Long lotId) {
        List<PeseeLot> pesees = getPeseesByLot(lotId);
        if (pesees == null || pesees.size() < 2) {
            return null;
        }
        BigDecimal premier = pesees.get(0).getPoidsMoyen();
        BigDecimal dernier = pesees.get(pesees.size() - 1).getPoidsMoyen();
        if (premier == null || dernier == null) return null;
        return dernier.subtract(premier);
    }
}

