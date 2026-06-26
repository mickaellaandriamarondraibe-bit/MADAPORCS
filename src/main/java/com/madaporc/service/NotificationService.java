package com.madaporc.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class NotificationService {

    private final List<SseEmitter> clients = new CopyOnWriteArrayList<>();

    public SseEmitter ajouterClient() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        clients.add(emitter);
        emitter.onCompletion(() -> clients.remove(emitter));
        emitter.onTimeout(() -> clients.remove(emitter));
        emitter.onError(erreur -> clients.remove(emitter));
        return emitter;
    }

    public void envoyerNotification(String message) {
        for (SseEmitter client : clients) {
            try {
                client.send(SseEmitter.event().name("notification").data(message));
            } catch (IOException exception) {
                clients.remove(client);
            }
        }
    }
}
