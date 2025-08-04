package com.example.model;

import com.example.strategy.CalculoPremio;


public class Aposta {
    private String numeroAposta;
    private double valor;
    private CalculoPremio estrategiaPremio;
    private String tipoAposta;

    public Aposta(String numeroAposta, double valor, CalculoPremio estrategiaPremio, String tipoAposta) {
        this.numeroAposta = numeroAposta;
        this.valor = valor;
        this.estrategiaPremio = estrategiaPremio;
        this.tipoAposta = tipoAposta;
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

    @Override
    public String toString() {
        return String.format("Aposta: %s | Tipo: %s | Valor: R$%.2f | Premio Possivel: R$%.2f",
                numeroAposta, tipoAposta, valor, calcularPremio());
    }
}