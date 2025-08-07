package com.example.model;
import com.example.strategy.CalculoPremio;
import com.example.strategy.PremioDezena;
import com.example.strategy.PremioGrupo;
import com.example.strategy.PremioMilhar;
public class ApostaFactory {
    public static Aposta criarAposta(String tipo, String numeroAposta, double valor, String cpfApostador) {
        CalculoPremio estrategia = null;
        
        switch(tipo.toLowerCase()) {
            case "grupo":
                estrategia = new PremioGrupo();
                break;
            case "dezena":
                estrategia = new PremioDezena();
                break;
            case "milhar":
                estrategia = new PremioMilhar();
                break;
            default:
                throw new IllegalArgumentException("Tipo de aposta inválido");
        }
        
        return new Aposta(numeroAposta, valor, estrategia, tipo, cpfApostador);
    }
}
