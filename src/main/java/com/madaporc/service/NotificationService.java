package com.madaporc.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class NotificationService {

    // Liste de tous les clients connectés
    private final List<SseEmitter> clients = new CopyOnWriteArrayList<>();

    // Un client se connecte → on l'ajoute à la liste
    public SseEmitter ajouterClient() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        clients.add(emitter);

        // Nettoyage quand le client se déconnecte
        emitter.onCompletion(() -> clients.remove(emitter));
        emitter.onTimeout(() -> clients.remove(emitter));

        return emitter;
    }

    // Envoie la notif à TOUS les clients connectés
    public void envoyerNotification(String message) {
        for (SseEmitter client : clients) {
            try {
                client.send(SseEmitter.event()
                    .name("notification")
                    .data(message));
            } catch (Exception e) {
                // On attrape TOUTE erreur (client deconnecte : IOException, ou emitter
                // deja termine : IllegalStateException) pour ne jamais casser l'appelant
                // (ex. la tache planifiee d'alertes).
                clients.remove(client);
            }
        }
    }
}
