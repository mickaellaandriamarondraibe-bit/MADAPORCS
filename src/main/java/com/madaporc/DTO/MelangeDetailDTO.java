package com.madaporc.DTO;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MelangeDetailDTO {
    private Long id;
    private String libelle;
    private String description;
    private BigDecimal coutKg;
    private String statut;
    
    @Getter
    @Setter
    public static class IngredientDetail {
        private String nomIngredient;
        private BigDecimal quantiteKg;
        private BigDecimal pourcentage;
        private BigDecimal coutLigne;
    }

    private List<IngredientDetail> ingredientsDetails;
}