package com.madaporc.DTO;


import lombok.Data;

@Data
public class UtilisateurDTO {
    private Long id;

private String nom;

private String email;

private String motDePasse;

private Long roleId;

private Long statutUtilisateurId;
}
