package com.example.model;

public class Resultado {
    private int milhar; // número de 0000 a 9999
    private int dezena; // dois últimos dígitos
    private String animal; // animal correspondente à dezena
    private double premioPrincipal;

    public Resultado(int milhar, String animal, double premioPrincipal) {
        this.milhar = milhar;
        this.dezena = (milhar % 100 == 0) ? 100 : milhar % 100;
        this.animal = animal;
        this.premioPrincipal = premioPrincipal;
    }

    // Getters
    public int getMilhar() {
        return milhar;
    }

    public int getDezena() {
        return dezena;
    }

    public String getAnimal() {
        return animal;
    }

    public double getPremioPrincipal() {
        return premioPrincipal;
    }

    public String getMilharFormatado() {
        return String.format("%04d", milhar);
    }

    public String getDezenaFormatada() {
        return (dezena == 100) ? "00" : String.format("%02d", dezena);
    }

    @Override
    public String toString() {
        return String.format("Milhar: %s | Dezena: %s | Animal: %s | Prêmio: R$%.2f",
                getMilharFormatado(), getDezenaFormatada(), animal, premioPrincipal);
    }
}
