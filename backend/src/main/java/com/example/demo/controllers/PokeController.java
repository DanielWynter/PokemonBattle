package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.model.PokeEntity;
import com.example.demo.services.PokeService;

@RestController
public class PokeController {

    @Autowired
    private PokeService pokeService;

    @GetMapping("/battle/stream")
    public SseEmitter streamBatalla() {
        return pokeService.agregarCliente();
    }

    @GetMapping("/api/pokemon/vida")
    public PokeEntity getVida() throws Exception {
        return pokeService.obtenerDesdeDB();
    }

    // 🔥 FIX: devolver JSON en vez de String
    @GetMapping("/api/pokemon/ataque")
    public PokeEntity atacar(
            @RequestParam String atacante,
            @RequestParam int dano
    ) throws Exception {
        pokeService.atacar(atacante, dano);

        // 🔥 devolver estado actualizado
        return pokeService.obtenerDesdeDB();
    }
}