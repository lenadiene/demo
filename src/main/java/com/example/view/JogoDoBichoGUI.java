package com.example.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.example.controller.ApostaController;
import com.example.model.Aposta;
import com.example.model.Resultado;
import com.example.observer.ApostadorObserver;
import com.example.observer.SorteioObservable;

public class JogoDoBichoGUI extends JFrame implements ApostadorObserver {
    private SorteioObservable sorteio;
    private ApostaController apostaController;
    private Map<String, ImageIcon> animaisImagens;
    private JLabel resultadoLabel;
    private JLabel imagemSelecionadaLabel;
    private DefaultListModel<String> listaApostasModel;
    private JList<String> listaApostas;
    private List<Aposta> apostas;
    private JTextArea tabelaNumerosAnimais;
private static final Map<String, Integer> mapaAnimalParaGrupo = new HashMap<>();

static {
    String[] nomes = {
        "Avestruz", "Águia", "Burro", "Borboleta", "Cachorro",
        "Cabra", "Carneiro", "Camelo", "Cobra", "Coelho",
        "Cavalo", "Elefante", "Galo", "Gato", "Jacaré",
        "Leão", "Macaco", "Porco", "Pavão", "Peru",
        "Touro", "Tigre", "Urso", "Veado", "Vaca"
    };
    for (int i = 0; i < nomes.length; i++) {
        mapaAnimalParaGrupo.put(nomes[i].toLowerCase(), i + 1);
    }
}

    public JogoDoBichoGUI() {
        super("Jogo do Bicho");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        sorteio = new SorteioObservable();
        sorteio.addObserver(this);
        apostaController = new ApostaController(sorteio);

        apostas = new ArrayList<>();

        carregarImagens();
        initUI();
    }

    private void carregarImagens() {
        animaisImagens = new HashMap<>();
        String[] nomesAnimais = {
            "avestruz", "aguia", "burro", "borboleta", "cachorro",
            "cabra", "carneiro", "camelo", "cobra", "coelho",
            "cavalo", "elefante", "galo", "gato", "jacare",
            "leao", "macaco", "porco", "pavao", "peru",
            "touro", "tigre", "urso", "veado", "vaca"
        };

        for (String nome : nomesAnimais) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/images/" + nome + ".jpg"));
                animaisImagens.put(capitalize(nome), icon);
            } catch (Exception e) {
                System.err.println("Imagem não encontrada: " + nome);
                animaisImagens.put(capitalize(nome), criarIconePlaceholder(capitalize(nome)));
            }
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
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

    private void initUI() {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelPrincipal.setBackground(Color.WHITE);

        JPanel controlPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        controlPanel.setBackground(Color.WHITE);

        JPanel tipoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tipoPanel.setBackground(Color.WHITE);
        tipoPanel.add(new JLabel("Tipo de aposta:"));
        String[] tipos = {"Grupo", "Dezena", "Milhar"};
        JComboBox<String> tipoCombo = new JComboBox<>(tipos);
        tipoPanel.add(tipoCombo);

        JPanel numeroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        numeroPanel.setBackground(Color.WHITE);
        numeroPanel.add(new JLabel("Número/Animal:"));
        JTextField numeroField = new JTextField(10);
        numeroPanel.add(numeroField);

        JPanel valorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        valorPanel.setBackground(Color.WHITE);
        valorPanel.add(new JLabel("Valor: R$"));
        JTextField valorField = new JTextField(10);
        valorPanel.add(valorField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton apostarButton = new JButton("Fazer Aposta");
        JButton sortearButton = new JButton("Realizar Sorteio");
        JButton limparButton = new JButton("Limpar Apostas");

        buttonPanel.add(apostarButton);
        buttonPanel.add(sortearButton);
        buttonPanel.add(limparButton);

        controlPanel.add(tipoPanel);
        controlPanel.add(numeroPanel);
        controlPanel.add(valorPanel);
        controlPanel.add(buttonPanel);

        JPanel resultadoPanel = new JPanel(new BorderLayout());
        resultadoPanel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        resultadoPanel.setBackground(Color.WHITE);
        resultadoLabel = new JLabel("Aguardando sorteio...", JLabel.CENTER);
        resultadoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        resultadoPanel.add(resultadoLabel, BorderLayout.NORTH);

        imagemSelecionadaLabel = new JLabel();
        imagemSelecionadaLabel.setHorizontalAlignment(JLabel.CENTER);
        resultadoPanel.add(imagemSelecionadaLabel, BorderLayout.CENTER);

        listaApostasModel = new DefaultListModel<>();
        listaApostas = new JList<>(listaApostasModel);
        JScrollPane scrollApostas = new JScrollPane(listaApostas);
        scrollApostas.setBorder(BorderFactory.createTitledBorder("Apostas Realizadas"));

        tabelaNumerosAnimais = new JTextArea();
        tabelaNumerosAnimais.setEditable(false);
        tabelaNumerosAnimais.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tabelaNumerosAnimais.setText("01-04: Avestruz\n05-08: Águia\n09-12: Burro\n13-16: Borboleta\n17-20: Cachorro\n"
            + "21-24: Cabra\n25-28: Carneiro\n29-32: Camelo\n33-36: Cobra\n37-40: Coelho\n"
            + "41-44: Cavalo\n45-48: Elefante\n49-52: Galo\n53-56: Gato\n57-60: Jacaré\n"
            + "61-64: Leão\n65-68: Macaco\n69-72: Porco\n73-76: Pavão\n77-80: Peru\n"
            + "81-84: Touro\n85-88: Tigre\n89-92: Urso\n93-96: Veado\n97-00: Vaca");
        JScrollPane tabelaScroll = new JScrollPane(tabelaNumerosAnimais);
        tabelaScroll.setBorder(BorderFactory.createTitledBorder("Tabela de Números e Animais"));

        apostarButton.addActionListener(e -> {
            try {
                String tipo = (String) tipoCombo.getSelectedItem();
                String numero = numeroField.getText().trim();
                String valorStr = valorField.getText().trim();

                if (numero.isEmpty() || valorStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double valor = Double.parseDouble(valorStr);
                Aposta aposta = apostaController.criarAposta(tipo, numero, valor);
                apostas.add(aposta);
                listaApostasModel.addElement(aposta.toString());

                String nomeAnimal = capitalize(numero.toLowerCase());
                ImageIcon imagem = animaisImagens.getOrDefault(nomeAnimal, criarIconePlaceholder(nomeAnimal));
                imagemSelecionadaLabel.setIcon(imagem);

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Digite um valor numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        sortearButton.addActionListener(e -> {
            apostaController.realizarSorteio();
        });

        limparButton.addActionListener(e -> {
            apostas.clear();
            listaApostasModel.clear();
        });

        painelPrincipal.add(controlPanel);
        painelPrincipal.add(resultadoPanel);
        painelPrincipal.add(scrollApostas);
        painelPrincipal.add(tabelaScroll);

        add(painelPrincipal, BorderLayout.CENTER);
    }

    @Override
public void update(Resultado resultado) {
    String mensagem = String.format(
        "<html><center>Resultado:<br>Milhar: %s<br>Dezena: %s<br>Animal: %s<br>Prêmio: R$%.2f</center></html>",
        resultado.getMilharFormatado(),
        resultado.getDezenaFormatada(),
        resultado.getAnimal(),
        resultado.getPremioPrincipal()
    );
    resultadoLabel.setText(mensagem);
    resultadoLabel.setIcon(null);
    imagemSelecionadaLabel.setIcon(animaisImagens.get(resultado.getAnimal()));

    boolean houveVencedor = apostas.stream().anyMatch(aposta -> {
        String tipo = aposta.getTipoAposta();
        String valorApostado = aposta.getNumeroAposta().trim().toLowerCase();

        try {
            switch (tipo.toLowerCase()) {
                case "milhar":
                    return Integer.parseInt(valorApostado) == resultado.getMilhar();

                case "dezena":
                    return Integer.parseInt(valorApostado) == resultado.getDezena();

                case "grupo":
                    int grupoResultado = (resultado.getDezena() == 100) ? 25 : ((resultado.getDezena() - 1) / 4 + 1);
                    Integer grupoAposta = null;

                    try {
                        grupoAposta = Integer.parseInt(valorApostado);
                    } catch (NumberFormatException e) {
                        grupoAposta = mapaAnimalParaGrupo.get(valorApostado);
                    }

                    return grupoAposta != null && grupoAposta == grupoResultado;

                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    });

    if (houveVencedor) {
        JOptionPane.showMessageDialog(this, "Parabéns! Uma das apostas foi vencedora!", "Resultado", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "Nenhuma aposta foi vencedora.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JogoDoBichoGUI jogo = new JogoDoBichoGUI();
            jogo.setVisible(true);
        });
    }
}
