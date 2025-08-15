package com.example.repository;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class AnimalRepository {

    private final String[] nomesAnimais = {
            "Avestruz", "Águia", "Burro", "Borboleta", "Cachorro",
            "Cabra", "Carneiro", "Camelo", "Cobra", "Coelho",
            "Cavalo", "Elefante", "Galo", "Gato", "Jacaré",
            "Leão", "Macaco", "Porco", "Pavão", "Peru",
            "Touro", "Tigre", "Urso", "Veado", "Vaca"
    };

    private final Map<String, Integer> animalParaGrupo = new HashMap<>();
    private final Map<String, ImageIcon> imagensAnimais = new HashMap<>();

    public AnimalRepository() {
        for (int i = 0; i < nomesAnimais.length; i++) {
            String nome = normalizarNome(nomesAnimais[i]);
            animalParaGrupo.put(nome, i + 1);
            imagensAnimais.put(nome, carregarImagem(nome));
        }
    }

    private String normalizarNome(String nome) {
        if (nome == null) return null;
        String semAcento = Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return semAcento.toLowerCase();
    }

    private ImageIcon carregarImagem(String nomeNormalizado) {
        try {
            return new ImageIcon(getClass().getResource("/images/" + nomeNormalizado + ".jpg"));
        } catch (Exception e) {
            return criarIconePlaceholder(capitalize(nomeNormalizado));
        }
    }

    private ImageIcon criarIconePlaceholder(String nome) {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, 100, 100);
        g2d.setColor(Color.BLACK);
        g2d.drawString(nome, 10, 50);
        g2d.dispose();
        return new ImageIcon(image);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Retorna todos os nomes normalizados para o combo
    public String[] getTodosAnimais() {
        String[] todos = new String[nomesAnimais.length];
        for (int i = 0; i < nomesAnimais.length; i++) {
            todos[i] = capitalize(normalizarNome(nomesAnimais[i]));
        }
        return todos;
    }

    // Retorna grupo a partir do nome do animal
    public Integer getGrupo(String nomeAnimal) {
        return animalParaGrupo.get(normalizarNome(nomeAnimal));
    }

    // Retorna imagem do animal
    public ImageIcon getImagem(String nomeAnimal) {
        return imagensAnimais.getOrDefault(normalizarNome(nomeAnimal), criarIconePlaceholder(nomeAnimal));
    }

    // Retorna dezenas do grupo (automático)
    public String getDezenas(int grupo) {
        int inicio = (grupo - 1) * 4 + 1;
        int fim = inicio + 3;
        return String.format("%02d a %02d", inicio, fim);
    }
}