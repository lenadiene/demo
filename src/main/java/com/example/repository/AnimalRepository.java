package com.example.repository;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class AnimalRepository {
    private final Map<String, ImageIcon> imagens = new HashMap<>();
    private final Map<String, Integer> mapaAnimalParaGrupo = new HashMap<>();

    public AnimalRepository() {
        inicializarAnimais();
        carregarImagens();
    }

    private void inicializarAnimais() {
        String[] nomes = {
                "Avestruz", "Águia", "Burro", "Borboleta", "Cachorro",
                "Cabra", "Carneiro", "Camelo", "Cobra", "Coelho",
                "Cavalo", "Elefante", "Galo", "Gato", "Jacaré",
                "Leão", "Macaco", "Porco", "Pavão", "Peru",
                "Touro", "Tigre", "Urso", "Veado", "Vaca"
        };
        for (int i = 0; i < nomes.length; i++) {
            mapaAnimalParaGrupo.put(normalizarNome(nomes[i]), i + 1);
        }
    }

    private void carregarImagens() {
        for (String nome : mapaAnimalParaGrupo.keySet()) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/images/" + nome.toLowerCase() + ".jpg"));
                imagens.put(nome, icon);
            } catch (Exception e) {
                imagens.put(nome, criarIconePlaceholder(nome));
            }
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

    public String normalizarNome(String nome) {
        if (nome == null) return null;
        String semAcento = Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        return capitalize(semAcento.toLowerCase());
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public Integer getGrupo(String nomeAnimal) {
        return mapaAnimalParaGrupo.get(normalizarNome(nomeAnimal));
    }

    public ImageIcon getImagem(String nomeAnimal) {
        return imagens.getOrDefault(normalizarNome(nomeAnimal), criarIconePlaceholder(nomeAnimal));
    }

    public String[] getTodosAnimais() {
        return mapaAnimalParaGrupo.keySet().stream().map(this::capitalize).toArray(String[]::new);
    }
}
