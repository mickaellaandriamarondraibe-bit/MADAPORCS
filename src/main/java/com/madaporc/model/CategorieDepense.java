package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categories_depenses")
public class CategorieDepense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
private String libelle;
}
