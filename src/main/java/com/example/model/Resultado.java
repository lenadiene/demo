package com.example.model;
import java.util.ArrayList;
import java.util.List;

public class Resultado {
    private int numeroSorteado;
    private String animal;
    private double premio; // prêmio unitário
    private List<Aposta> ganhadoras;
    private double premioTotal; // soma de todos os prêmios das apostas ganhadoras

    public Resultado(int numeroSorteado, String animal, double premio, List<Aposta> todasApostas) {
        this.numeroSorteado = numeroSorteado;
        this.animal = animal;
        this.premio = premio;
        this.ganhadoras = new ArrayList<>();
        this.premioTotal = 0;
        verificarGanhadores(todasApostas);  // ← PROCESSAMENTO DAS APOSTAS PARA SABER QUAIS GANHARAM
    }

    private void verificarGanhadores(List<Aposta> todasApostas) {
    for (Aposta aposta : todasApostas) {
        String tipo = aposta.getTipoAposta().toLowerCase();
        String valorAposta = aposta.getNumeroAposta().trim().toLowerCase();

        boolean venceu = false;
        int dezenaSorteada = numeroSorteado % 100;
        if (dezenaSorteada == 0) dezenaSorteada = 100;

        switch (tipo) {
            case "milhar":
                venceu = valorAposta.equals(String.format("%04d", numeroSorteado));
                break;
                
            case "dezena":
                venceu = valorAposta.equals(String.format("%02d", dezenaSorteada));
                break;
                
            case "grupo": // CORRIGIDO: era "animal", mas o tipo é "Grupo"
                // Converter número para animal ou verificar diretamente
                 String animalDaApostaNormalizado = removerAcentos(valorAposta);
                String animalSorteadoNormalizado = removerAcentos(animal.toLowerCase());
                venceu = animalDaApostaNormalizado.equals(animalSorteadoNormalizado);
                break;
        }

        if (venceu) {
            ganhadoras.add(aposta);
            premioTotal += aposta.calcularPremio();
        }
    }
}
private String removerAcentos(String texto) {
    return texto.replaceAll("[áàâãä]", "a")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[íìîï]", "i")
                .replaceAll("[óòôõö]", "o")
                .replaceAll("[úùûü]", "u")
                .replaceAll("[ç]", "c");
}
    public int getNumeroSorteado() {
        return numeroSorteado;
    }

    public String getAnimal() {
        return animal;
    }

    public double getPremio() {
        return premio;
    }

    public List<Aposta> getGanhadoras() {
        return ganhadoras;
    }

    public double getPremioTotal() {
        return premioTotal;
    }
}
