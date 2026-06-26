package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "vaccinations")
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lot_id", nullable = false)
    private LotPorc lot;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vaccin_id", nullable = false)
    private Vaccin vaccin;

    @Column(name = "date_vaccination", nullable = false)
    private LocalDate dateVaccination;

    @Column(name = "date_rappel")
    private LocalDate dateRappel;

    @Column(name = "observation")
    private String observation;

    // champs affichage JSP
    @Transient
    public Long getLotId() {
        return lot != null ? lot.getId() : null;
    }

    @Transient
    public String getCodeLot() {
        return lot != null ? lot.getCodeLot() : null;
    }

    @Transient
    public String getNomVaccin() {
        return vaccin != null ? vaccin.getNom() : null;
    }

    @Transient
    public String getStatut() {
        // JSP attend 'A_VENIR' / autre. Simplification : si dateRappel null => 'REALISE', sinon calcul.
        if (dateRappel == null) return "REALISE";
        return dateRappel.isAfter(LocalDate.now()) ? "A_VENIR" : "REALISE";
    }
}

