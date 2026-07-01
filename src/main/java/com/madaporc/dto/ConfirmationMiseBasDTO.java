package com.madaporc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class ConfirmationMiseBasDTO {

    @NotNull(message = "La date réelle de mise bas est obligatoire.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateMiseBasReelle;

    @NotNull(message = "Le nombre de femelles gestantes est obligatoire.")
    @Min(value = 0, message = "Le nombre de femelles gestantes ne peut pas être négatif.")
    private Integer nbFemellesGestantes;

    @NotNull(message = "Le nombre de femelles non gestantes est obligatoire.")
    @Min(value = 0, message = "Le nombre de femelles non gestantes ne peut pas être négatif.")
    private Integer nbFemellesNonGestantes;

    @NotNull(message = "Le nombre de femelles ayant mis bas est obligatoire.")
    @Min(value = 0, message = "Le nombre de femelles ayant mis bas ne peut pas être négatif.")
    private Integer nbFemellesMiseBas;

    @NotNull(message = "Le nombre de porcelets nés est obligatoire.")
    @Min(value = 0, message = "Le nombre de porcelets nés ne peut pas être négatif.")
    private Integer nbPorceletsNes;

    @NotNull(message = "Le nombre de porcelets vivants est obligatoire.")
    @Min(value = 0, message = "Le nombre de porcelets vivants ne peut pas être négatif.")
    private Integer nbPorceletsVivants;

    @NotNull(message = "Le nombre de porcelets morts est obligatoire.")
    @Min(value = 0, message = "Le nombre de porcelets morts ne peut pas être négatif.")
    private Integer nbPorceletsMorts;

    // --- Répartition en lot(s) naissance ---
    // Champs de formulaire uniquement : NON stockés en base, servent à créer les lots.
    @Min(value = 0, message = "Le nombre de femelles ne peut pas être négatif.")
    private Integer nbFemelles = 0;

    @Min(value = 0, message = "Le nombre de mâles ne peut pas être négatif.")
    private Integer nbMales = 0;

    private String objectifLot = "ENGRAISSEMENT";

    private String observation;
}