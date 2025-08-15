package com.example.controller;

import com.example.model.Aposta;
import com.example.model.ApostaFactory;
import com.example.model.Sorteio;
import com.example.observer.SorteioObservable;

public class ApostaController {
    private SorteioObservable sorteioObservable;
    
    public ApostaController(SorteioObservable sorteioObservable) {
        this.sorteioObservable = sorteioObservable;
    }
    
    public Aposta criarAposta(String tipo, String numero, double valor, String cpf) {
        return ApostaFactory.criarAposta(tipo, numero, valor, cpf);
    }
    
    public void realizarSorteio() {
        sorteioObservable.setResultado(Sorteio.realizarSorteio());
    }
}
