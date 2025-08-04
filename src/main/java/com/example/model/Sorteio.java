package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class Sorteio {
    private static final List<Animal> ANIMAIS = new ArrayList<>();
    
    static {
        // Inicializa a lista de animais
        ANIMAIS.add(new Animal("Avestruz", 1, 4));
        ANIMAIS.add(new Animal("Águia", 5, 8));
        ANIMAIS.add(new Animal("Burro", 9, 12));
        ANIMAIS.add(new Animal("Borboleta", 13, 16));
        ANIMAIS.add(new Animal("Cachorro", 17, 20));
        ANIMAIS.add(new Animal("Cabra", 21, 24));
        ANIMAIS.add(new Animal("Carneiro", 25, 28));
        ANIMAIS.add(new Animal("Camelo", 29, 32));
        ANIMAIS.add(new Animal("Cobra", 33, 36));
        ANIMAIS.add(new Animal("Coelho", 37, 40));
        ANIMAIS.add(new Animal("Cavalo", 41, 44));
        ANIMAIS.add(new Animal("Elefante", 45, 48));
        ANIMAIS.add(new Animal("Galo", 49, 52));
        ANIMAIS.add(new Animal("Gato", 53, 56));
        ANIMAIS.add(new Animal("Jacaré", 57, 60));
        ANIMAIS.add(new Animal("Leão", 61, 64));
        ANIMAIS.add(new Animal("Macaco", 65, 68));
        ANIMAIS.add(new Animal("Porco", 69, 72));
        ANIMAIS.add(new Animal("Pavão", 73, 76));
        ANIMAIS.add(new Animal("Peru", 77, 80));
        ANIMAIS.add(new Animal("Touro", 81, 84));
        ANIMAIS.add(new Animal("Tigre", 85, 88));
        ANIMAIS.add(new Animal("Urso", 89, 92));
        ANIMAIS.add(new Animal("Veado", 93, 96));
        ANIMAIS.add(new Animal("Vaca", 97, 100));
    }

    public static Resultado realizarSorteio() {
    int milhar = (int) (Math.random() * 10000); // 0000 a 9999
    int dezena = milhar % 100; // pega os dois últimos dígitos
    if (dezena == 0) dezena = 100; // trata o caso 00 como 100 (Grupo 25)

    String animal = determinarAnimal(dezena);
    double premio = 10000 + (Math.random() * 90000); // Prêmio entre 10k e 100k

    return new Resultado(milhar, animal, premio); // Agora milhar é o número do sorteio
}


    private static String determinarAnimal(int numero) {
        if (numero == 0) numero = 100; // Tratamento especial para o 00
        
        for (Animal animal : ANIMAIS) {
            if (animal.contemNumero(numero)) {
                return animal.getNome();
            }
        }
        
        return "Desconhecido";
    }

    public static List<Animal> getAnimais() {
        return new ArrayList<>(ANIMAIS);
    }
}