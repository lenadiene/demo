package com.example.strategy;

public class PremioMilhar implements CalculoPremio {
    @Override
    public double calcularPremio(double valorAposta) {
        return valorAposta * 4000;
    }
}