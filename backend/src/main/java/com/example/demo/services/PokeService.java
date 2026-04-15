package com.example.demo.services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.model.PokeEntity;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;

import jakarta.annotation.PostConstruct;

@Service
public class PokeService {

    @Autowired
    private FirebaseApp firebaseApp;

    private volatile PokeEntity ultimaBatalla = new PokeEntity(100, 100);
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void escucharCambios() {
        Firestore db = FirestoreClient.getFirestore(firebaseApp);

        DocumentReference docRef = db.collection("pokebattles").document("battle1");

        docRef.addSnapshotListener((snapshot, error) -> {
            if (error != null) return;

            if (snapshot != null && snapshot.exists()) {
                ultimaBatalla = snapshot.toObject(PokeEntity.class);
                enviarActualizacion();
            }
        });
    }

    // 🔥 SSE
    public SseEmitter agregarCliente() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        try {
            emitter.send(ultimaBatalla);
        } catch (IOException e) {
            emitters.remove(emitter);
        }

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        return emitter;
    }

    public PokeEntity obtenerDesdeDB() throws Exception {
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        DocumentReference docRef = db.collection("pokebattles").document("battle1");

        return docRef.get().get().toObject(PokeEntity.class);
    }

    // 🔥 FIX CLAVE
    public void atacar(String atacante, int dano) throws Exception {
        Firestore db = FirestoreClient.getFirestore(firebaseApp);
        DocumentReference docRef = db.collection("pokebattles").document("battle1");

        PokeEntity actual = docRef.get().get().toObject(PokeEntity.class);
        if (actual == null) return;

        if (atacante.equals("p1")) {
            actual.setVida2(Math.max(0, actual.getVida2() - dano));
        } else if (atacante.equals("p2")) {
            actual.setVida1(Math.max(0, actual.getVida1() - dano));
        }

        docRef.set(actual);

        // 🔥 IMPORTANTE: actualizar inmediatamente
        ultimaBatalla = actual;
        enviarActualizacion();
    }

    private void enviarActualizacion() {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(ultimaBatalla);
            } catch (IOException | IllegalStateException e) {
                emitters.remove(emitter);
            }
        }
    }
}