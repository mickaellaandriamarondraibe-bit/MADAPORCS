package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "imports_exports")
public class ImportExport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    // IMPORT ou EXPORT
    @Column(name = "type_operation", nullable = false, length = 20)
    private String typeOperation;

    // EXCEL ou PDF
    @Column(name = "format_fichier", nullable = false, length = 20)
    private String formatFichier;

    @Column(nullable = false, length = 100)
    private String module;

    @Column(name = "nom_fichier")
    private String nomFichier;

    @Column(name = "date_operation", nullable = false)
    private LocalDateTime dateOperation;

    // SUCCES ou ECHEC
    @Column(nullable = false, length = 20)
    private String statut = "SUCCES";

    @Column(columnDefinition = "TEXT")
    private String message;

    @PrePersist
    void onCreate() {
        if (dateOperation == null) dateOperation = LocalDateTime.now();
    }

    // Date deja formatee pour l'affichage JSP (fmt:formatDate ne gere pas LocalDateTime)
    @Transient
    public String getDateFormatee() {
        return dateOperation == null ? ""
                : dateOperation.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
