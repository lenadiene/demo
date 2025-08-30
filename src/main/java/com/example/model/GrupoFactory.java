package com.example.model;

import com.example.strategy.PremioGrupo;

public class GrupoFactory implements ApostaFactory {

    @Override
    public Aposta criarAposta(String numeroAposta, double valor, String cpfApostador) {
        return new Aposta(numeroAposta, valor, new PremioGrupo(), "GRUPO", cpfApostador);
    }
}
