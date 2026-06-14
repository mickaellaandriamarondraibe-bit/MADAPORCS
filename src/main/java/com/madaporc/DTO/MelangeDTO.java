package com.madaporc.DTO;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MelangeDTO {
    private Long id;
    private String libelle;
    private String description;
    private List<MelangeIngredientDTO> ingredients = new ArrayList<>();
}