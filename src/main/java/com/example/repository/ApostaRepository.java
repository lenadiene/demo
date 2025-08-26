package com.example.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.model.Aposta;
import com.example.strategy.PremioGrupo;

public class ApostaRepository {
    private final List<Aposta> apostas = new ArrayList<>();
    private final PremioGrupo estrategiaGrupo = new PremioGrupo();

    
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
