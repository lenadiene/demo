package com.example.model;

import com.example.strategy.PremioMilhar;

public class MilharFactory implements ApostaFactory {

    @Override
    public Aposta criarAposta(String numeroAposta, double valor, String cpfApostador) {
        return new Aposta(numeroAposta, valor, new PremioMilhar(), "MILHAR", cpfApostador);
    }
}
