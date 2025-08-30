package com.example.model;

import java.util.HashMap;
import java.util.Map;

public class GerenciadorFactories {
    private Map<String, ApostaFactory> factories = new HashMap<>();

    public GerenciadorFactories() {
        factories.put("GRUPO", new GrupoFactory());
        factories.put("DEZENA", new DezenaFactory());
        factories.put("MILHAR", new MilharFactory());
    }

    public void registrarFactory(String tipo, ApostaFactory factory) {
        factories.put(tipo.toUpperCase(), factory);
    }

    public Aposta criarAposta(String tipo, String numeroAposta, double valor, String cpfApostador) {
        ApostaFactory factory = factories.get(tipo.toUpperCase());
        if (factory == null) {
            throw new IllegalArgumentException("Tipo de aposta não suportado: " + tipo);
        }
        return factory.criarAposta(numeroAposta, valor, cpfApostador);
    }
}
