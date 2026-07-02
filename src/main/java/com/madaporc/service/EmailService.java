package com.madaporc.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final String expediteur;
    private final String motDePasse;
    private final String destinataireDefaut;
    private final Session session;

    // Constructeur — configure la connexion SMTP une seule fois
    public EmailService(@Value("${app.mail.expediteur:}") String expediteur,
                        @Value("${app.mail.mot-de-passe:}") String motDePasse,
                        @Value("${app.mail.destinataire:}") String destinataireDefaut) {
        this.expediteur = expediteur;
        this.motDePasse = motDePasse;
        this.destinataireDefaut = destinataireDefaut;

        if (expediteur == null || expediteur.isBlank() || motDePasse == null || motDePasse.isBlank()) {
            this.session = null;
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        this.session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(expediteur, motDePasse);
            }
        });
    }

    // Email texte simple
    public boolean envoyerTexte(String destinataire, String sujet, String corps) {
        if (!sessionPrete()) {
            return false;
        }
        try {
            String cible = normaliserDestinataire(destinataire);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(expediteur));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(cible));
            msg.setSubject(sujet);
            msg.setText(corps);
            Transport.send(msg);
            System.out.println("✓ Email texte envoyé à " + cible);
            return true;
        } catch (MessagingException e) {
            System.err.println("✗ Erreur : " + e.getMessage());
            return false;
        }
    }

    // Email HTML (plus joli, avec mise en forme)
    public boolean envoyerHTML(String destinataire, String sujet, String corpsHTML) {
        if (!sessionPrete()) {
            return false;
        }
        try {
            String cible = normaliserDestinataire(destinataire);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(expediteur));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(cible));
            msg.setSubject(sujet);
            msg.setContent(corpsHTML, "text/html; charset=utf-8");
            Transport.send(msg);
            System.out.println("✓ Email HTML envoyé à " + cible);
            return true;
        } catch (MessagingException e) {
            System.err.println("✗ Erreur : " + e.getMessage());
            return false;
        }
    }

    // Email avec pièce jointe
    public boolean envoyerAvecFichier(String destinataire, String sujet, String corps, String cheminFichier) {
        if (!sessionPrete()) {
            return false;
        }
        try {
            String cible = normaliserDestinataire(destinataire);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(expediteur));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(cible));
            msg.setSubject(sujet);

            // Partie texte
            MimeBodyPart texte = new MimeBodyPart();
            texte.setText(corps);

            // Partie fichier
            MimeBodyPart fichier = new MimeBodyPart();
            fichier.attachFile(cheminFichier);

            // Assemblage
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texte);
            multipart.addBodyPart(fichier);
            msg.setContent(multipart);

            Transport.send(msg);
            System.out.println("✓ Email avec pièce jointe envoyé à " + cible);
            return true;
        } catch (Exception e) {
            System.err.println("✗ Erreur : " + e.getMessage());
            return false;
        }
    }

    public String getDestinataireParDefaut() {
        return destinataireDefaut;
    }

    public boolean isConfigured() {
        return session != null;
    }

    private boolean sessionPrete() {
        return session != null
                && expediteur != null && !expediteur.isBlank()
                && motDePasse != null && !motDePasse.isBlank();
    }

    private String normaliserDestinataire(String destinataire) {
        if (destinataire != null && !destinataire.isBlank()) {
            return destinataire;
        }

        if (destinataireDefaut != null && !destinataireDefaut.isBlank()) {
            return destinataireDefaut;
        }

        return expediteur;
    }
}