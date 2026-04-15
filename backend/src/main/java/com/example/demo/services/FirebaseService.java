package com.example.demo.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class FirebaseService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseService.class);

    public String guardarDato() {

        try {

            // obtener instancia de Firestore
            Firestore db = FirestoreClient.getFirestore();

            // datos a guardar
            Map<String, Object> data = new HashMap<>();
            data.put("mensaje", "Conexion exitosa con Firestore");
            data.put("estado", "ok");

            // guardar en colección
            db.collection("test")
                .document("doc1")
                .set(data);

            return "Datos guardados correctamente";

        } catch (Exception e) {
            logger.error("Error al guardar datos en Firestore", e);
            return "Error al guardar datos";
        }
    }
}

