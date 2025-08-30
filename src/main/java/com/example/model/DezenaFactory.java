package com.example.model;

import com.example.strategy.PremioDezena;

public class DezenaFactory implements ApostaFactory {

    @Override
    public Aposta criarAposta(String numeroAposta, double valor, String cpfApostador) {
        return new Aposta(numeroAposta, valor, new PremioDezena(), "DEZENA", cpfApostador);
    }
}
