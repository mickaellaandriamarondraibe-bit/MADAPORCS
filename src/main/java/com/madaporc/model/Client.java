package com.madaporc.model;

    import jakarta.persistence.*;
import java.time.LocalDateTime;
    import lombok.Data;

    @Data
    @Entity
    @Table(name = "clients")
    public class Client {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "nom")
    private String nom;

    @Column(name = "type_client")
    private String typeClient;

    @Column(name = "contact")
    private String contact;

    @Column(name = "email")
    private String email;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    }
