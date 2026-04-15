package com.example.demo.model;

public class PokeEntity {
    private int vida1;
    private int vida2;

    public PokeEntity() {}

    public PokeEntity(int vida1, int vida2) {
        this.vida1 = vida1;
        this.vida2 = vida2;
    }

    public int getVida1() { return vida1; }
    public void setVida1(int vida1) { this.vida1 = vida1; }

    public int getVida2() { return vida2; }
    public void setVida2(int vida2) { this.vida2 = vida2; }
}