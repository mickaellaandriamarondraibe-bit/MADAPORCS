package com.madaporc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EvenementCalendrier {
    private String title;   // texte affiché
    private String start;   // date ISO "aaaa-mm-jj"
    private String type;    // misebas | depense | vente | mort
    private String color;   // couleur de la pastille
}
