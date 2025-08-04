package com.example.strategy;

public class PremioGrupo implements CalculoPremio {
    @Override
    public double calcularPremio(double valorAposta) {
        return valorAposta * 18;
    }
}