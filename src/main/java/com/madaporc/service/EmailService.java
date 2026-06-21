package com.madaporc.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {

    private final String expediteur;
    private final String motDePasse;
    private final Session session;

    // Constructeur — configure la connexion SMTP une seule fois
    public EmailService(String expediteur, String motDePasse) {
        this.expediteur = expediteur;
        this.motDePasse = motDePasse;

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
    public void envoyerTexte(String destinataire, String sujet, String corps) {
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(expediteur));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            msg.setSubject(sujet);
            msg.setText(corps);
            Transport.send(msg);
            System.out.println("✓ Email texte envoyé à " + destinataire);
        } catch (MessagingException e) {
            System.err.println("✗ Erreur : " + e.getMessage());
        }
    }

    // Email HTML (plus joli, avec mise en forme)
    public void envoyerHTML(String destinataire, String sujet, String corpsHTML) {
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(expediteur));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            msg.setSubject(sujet);
            msg.setContent(corpsHTML, "text/html; charset=utf-8");
            Transport.send(msg);
            System.out.println("✓ Email HTML envoyé à " + destinataire);
        } catch (MessagingException e) {
            System.err.println("✗ Erreur : " + e.getMessage());
        }
    }

    // Email avec pièce jointe
    public void envoyerAvecFichier(String destinataire, String sujet, String corps, String cheminFichier) {
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(expediteur));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
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
            System.out.println("✓ Email avec pièce jointe envoyé à " + destinataire);
        } catch (Exception e) {
            System.err.println("✗ Erreur : " + e.getMessage());
        }
    }

    // --- MAIN pour tester ---
    public static void main(String[] args) {

        EmailService service = new EmailService(
                "ton.email@gmail.com",
                "znbt mfaw yhwi tgtm" // Mot de passe d'application (avec ou sans espaces)
        );

        // Test 1 — Email simple
        // service.envoyerTexte(
        //         "destinataire@example.com",
        //         "Test Java Mail",
        //         "Bonjour ! Ceci est un email envoyé depuis Java.");

        // Test 2 — Email HTML
        service.envoyerHTML(
                "noahamitiafyh@gmail.com",
                "Notification Java",
                """
                        <div style="font-family: Arial; padding: 20px;">
                            <h2 style="color: #4A90D9;">🔔 Nouvelle notification</h2>
                            <p>Bonjour,</p>
                            <p>Voici un email <b>HTML</b> envoyé depuis Java ! Par l' appli madaporc</p>
                            <a href="https://example.com" style="background:#4A90D9; color:white; padding:10px 20px; border-radius:5px; text-decoration:none;">
                                Voir les détails
                            </a>
                        </div>
                        """);

        // Test 3 — Avec pièce jointe
        // service.envoyerAvecFichier(
        //         "destinataire@example.com",
        //         "Rapport Java",
        //         "Bonjour, veuillez trouver le rapport en pièce jointe.",
        //         "C:/Users/toi/Documents/rapport.pdf");
    }
}