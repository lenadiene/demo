package com.example.repository;

import com.example.model.Aposta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApostaRepository {
    private final List<Aposta> apostas = new ArrayList<>();

    public void salvar(Aposta aposta) {
        apostas.add(aposta);
    }

    public List<Aposta> listar() {
        return Collections.unmodifiableList(apostas);
    }

    public void limpar() {
        apostas.clear();
    }
}
