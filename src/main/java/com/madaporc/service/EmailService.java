package com.madaporc.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final String expediteur;
    private final String motDePasse;
    private final String host;
    private final String port;

    public EmailService(@Value("${madaporc.mail.expediteur:}") String expediteur,
                        @Value("${madaporc.mail.mot-de-passe:}") String motDePasse,
                        @Value("${madaporc.mail.host:smtp.gmail.com}") String host,
                        @Value("${madaporc.mail.port:587}") String port) {
        this.expediteur = expediteur;
        this.motDePasse = motDePasse;
        this.host = host;
        this.port = port;
    }

    public void envoyerTexte(String destinataire, String sujet, String corps) {
        if (!configurationValide(destinataire)) {
            return;
        }

        try {
            Message message = new MimeMessage(creerSession());
            message.setFrom(new InternetAddress(expediteur));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setText(corps);
            Transport.send(message);
        } catch (MessagingException exception) {
            System.err.println("Erreur lors de l'envoi de l'email MADAPORC : " + exception.getMessage());
        }
    }

    public void envoyerHtml(String destinataire, String sujet, String corpsHtml) {
        if (!configurationValide(destinataire)) {
            return;
        }

        try {
            Message message = new MimeMessage(creerSession());
            message.setFrom(new InternetAddress(expediteur));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setContent(corpsHtml, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException exception) {
            System.err.println("Erreur lors de l'envoi de l'email MADAPORC : " + exception.getMessage());
        }
    }

    private Session creerSession() {
        Properties proprietes = new Properties();
        proprietes.put("mail.smtp.auth", "true");
        proprietes.put("mail.smtp.starttls.enable", "true");
        proprietes.put("mail.smtp.host", host);
        proprietes.put("mail.smtp.port", port);

        return Session.getInstance(proprietes, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(expediteur, motDePasse);
            }
        });
    }

    private boolean configurationValide(String destinataire) {
        return destinataire != null && !destinataire.isBlank()
                && expediteur != null && !expediteur.isBlank()
                && motDePasse != null && !motDePasse.isBlank();
    }
}
