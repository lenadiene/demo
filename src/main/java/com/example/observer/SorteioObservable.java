package com.example.observer;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Aposta;
import com.example.model.Resultado;

public class SorteioObservable {
    private List<ApostadorObserver> observers = new ArrayList<>();
    private Resultado resultado;

    public void addObserver(ApostadorObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(ApostadorObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers() {
        for (ApostadorObserver observer : observers) {
            observer.update(resultado);
        }
    }
    
    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
        notifyObservers();
    }

    public void addAposta(Aposta aposta) {
        notifyObserversAposta(aposta);
    }

    private void notifyObserversAposta(Aposta aposta) {
        for (ApostadorObserver observer : observers) {
            observer.novaAposta(aposta);
        }
    }
}