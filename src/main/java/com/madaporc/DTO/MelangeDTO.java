package com.madaporc.DTO;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class MelangeDTO {
    private Long id;
    private String libelle;
    private String description;
    private List<MelangeIngredientDTO> ingredients;
}
