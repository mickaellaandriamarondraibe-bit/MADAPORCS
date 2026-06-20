package com.madaporc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LotNaissanceDTO {

    @NotBlank(message = "Le code du lot naissance est obligatoire.")
    private String codeLot;

    @NotBlank(message = "Le sexe du lot naissance est obligatoire.")
    private String sexe;

    @NotBlank(message = "L'objectif du lot naissance est obligatoire.")
    private String objectif;

    @NotNull(message = "L'effectif initial est obligatoire.")
    @Min(value = 1, message = "L'effectif initial doit être supérieur à 0.")
    private Integer effectifInitial;

    private String description;
}