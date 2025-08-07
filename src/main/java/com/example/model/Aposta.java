package com.example.model;

import com.example.strategy.CalculoPremio;


public class Aposta {
    private String numeroAposta;
    private double valor;
    private CalculoPremio estrategiaPremio;
    private String tipoAposta;
private String cpfApostador;
    public Aposta(String numeroAposta, double valor, CalculoPremio estrategiaPremio, String tipoAposta, String cpfApostador) {
        this.numeroAposta = numeroAposta;
        this.valor = valor;
        this.estrategiaPremio = estrategiaPremio;
        this.tipoAposta = tipoAposta;
        this.cpfApostador = cpfApostador;
    }

    public double calcularPremio() {
        return estrategiaPremio.calcularPremio(valor);
    }

    // Getters
    public String getNumeroAposta() {
        return numeroAposta;
    }

    public double getValor() {
        return valor;
    }

    public String getTipoAposta() {
        return tipoAposta;
    }
    public String getCpfApostador() {
    return cpfApostador;
}

        @Override
    public String toString() {
        double premio = estrategiaPremio.calcularPremio(valor);
        return "CPF: " + cpfApostador +
               " | Tipo: " + tipoAposta +
               " | Número: " + numeroAposta +
               " | Valor: R$" + valor +
               " | Prêmio: R$" + String.format("%.2f", premio);
    }
}