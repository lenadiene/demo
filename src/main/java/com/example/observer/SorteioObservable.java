package com.example.observer;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Aposta;
import com.example.model.Resultado;
//mantém uma lista de ApostadorObserver, já que a interface implementa ApostadorObserver 
//Tem objetivo de notificar quem estiver observando sempre que há uma nova aposta ou um sorteio
public class SorteioObservable {
    private List<ApostadorObserver> observers = new ArrayList<>();
    private Resultado resultado;

    public void addObserver(ApostadorObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(ApostadorObserver observer) {
        observers.remove(observer);
    }
    //Cada observer recebe o update para atualizar a interface
    public void notifyObservers() {
        for (ApostadorObserver observer : observers) {
            observer.update(resultado);
        }
    }
    //Quando um Resultado é gerado, SorteioObservable chama notifyObservers() que atualiza todos os observadores
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