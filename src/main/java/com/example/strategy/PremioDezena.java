package com.example.strategy;

public class PremioDezena implements CalculoPremio {
    @Override
    public double calcularPremio(double valorAposta) {
        return valorAposta * 60;
    }
}