package com.madaporc.service;

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
            } catch (IOException e) {
                clients.remove(client); // client déconnecté
            }
        }
    }
}
