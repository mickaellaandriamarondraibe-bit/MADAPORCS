package com.madaporc.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Gestion centralisée des erreurs : évite les pages blanches (stack trace)
 * et affiche un message clair sur une page d'erreur propre.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // Ex. : saisir du texte dans un champ numérique (montant, quantité, id...).
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String typeInvalide(MethodArgumentTypeMismatchException e, Model model) {
        model.addAttribute("messageErreur",
                "Valeur invalide pour le champ « " + e.getName()
                        + " » : une valeur du bon type est attendue.");
        return "erreur";
    }

    // Règles de gestion / ressources introuvables signalées explicitement.
    @ExceptionHandler(IllegalArgumentException.class)
    public String argumentInvalide(IllegalArgumentException e, Model model) {
        model.addAttribute("messageErreur",
                e.getMessage() != null ? e.getMessage() : "Requête invalide.");
        return "erreur";
    }

    // Filet de sécurité : toute autre erreur non gérée → page propre, pas d'écran blanc.
    @ExceptionHandler(Exception.class)
    public String erreurGenerale(Exception e, Model model) {
        model.addAttribute("messageErreur",
                "Une erreur inattendue est survenue. Réessayez, ou revenez au tableau de bord.");
        return "erreur";
    }
}
