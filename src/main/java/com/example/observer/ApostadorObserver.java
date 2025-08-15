package com.example.observer;
import com.example.model.Aposta;
import com.example.model.Resultado;

public interface ApostadorObserver {
    void update(Resultado resultado);
    void novaAposta(Aposta aposta);
}