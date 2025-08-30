package com.example.model;

public interface ApostaFactory {
  Aposta criarAposta(String numeroAposta, double valor, String cpfApostador);
}
