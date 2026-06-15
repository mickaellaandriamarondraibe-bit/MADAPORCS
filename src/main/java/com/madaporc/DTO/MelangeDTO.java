package com.madaporc.DTO;

import java.util.List;
import lombok.Data;

@Data
public class MelangeDTO {
    private Long id;

private String libelle;

private String description;

private List<MelangeIngredientDTO> ingredients;
}
