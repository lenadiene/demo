package com.example.model;

public class Animal {
    private String nome;
    private int numeroInicial;
    private int numeroFinal;

    public Animal(String nome, int numeroInicial, int numeroFinal) {
        this.nome = nome;
        this.numeroInicial = numeroInicial;
        this.numeroFinal = numeroFinal;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public int getNumeroInicial() {
        return numeroInicial;
    }

    public int getNumeroFinal() {
        return numeroFinal;
    }

    public boolean contemNumero(int numero) {
        
        return numero >= numeroInicial && numero <= numeroFinal;
    }

    @Override
    public String toString() {
        return nome + " (" + numeroInicial + "-" + numeroFinal + ")";
    }
}