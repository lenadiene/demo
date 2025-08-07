package com.example.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.text.Normalizer;
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
import javax.swing.JTabbedPane;
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
    private JLabel imagemApostaLabel;
    private JLabel imagemResultadoLabel;
    private DefaultListModel<String> listaApostasModel;
    private JList<String> listaApostas;
    private List<Aposta> apostas;
    private JTextArea vencedoresTextArea;

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
    private String normalizarNomeAnimal(String nome) {
        if (nome == null) return null;
        String semAcento = Normalizer.normalize(nome, Normalizer.Form.NFD)
            .replaceAll("[^\\p{ASCII}]", "");
        return capitalize(semAcento.toLowerCase());
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
            animaisImagens.put(normalizarNomeAnimal(nome), icon); // <-- normalizado
        } catch (Exception e) {
            System.err.println("Imagem não encontrada: " + nome);
            animaisImagens.put(normalizarNomeAnimal(nome), criarIconePlaceholder(capitalize(nome)));
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

    private JPanel criarPainelSorteio() {
        JPanel painelSorteio = new JPanel();
        painelSorteio.setLayout(new BorderLayout(10, 10));
        painelSorteio.setBackground(Color.WHITE);

        JPanel resultadoPanel = new JPanel(new BorderLayout());
        resultadoPanel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        resultadoPanel.setBackground(Color.WHITE);
        resultadoLabel = new JLabel("Aguardando sorteio...", JLabel.CENTER);
        resultadoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        resultadoPanel.add(resultadoLabel, BorderLayout.NORTH);

        imagemResultadoLabel = new JLabel();
        imagemResultadoLabel.setHorizontalAlignment(JLabel.CENTER);
        resultadoPanel.add(imagemResultadoLabel, BorderLayout.CENTER);

        JButton sortearButton = new JButton("Realizar Sorteio");
        sortearButton.addActionListener(e -> apostaController.realizarSorteio());

        JPanel sortearPanel = new JPanel(new FlowLayout());
        sortearPanel.setBackground(Color.WHITE);
        sortearPanel.add(sortearButton);

        vencedoresTextArea = new JTextArea();
        vencedoresTextArea.setEditable(false);
        vencedoresTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        vencedoresTextArea.setBorder(BorderFactory.createTitledBorder("Apostas Vencedoras"));

        JScrollPane vencedoresScroll = new JScrollPane(vencedoresTextArea);

        painelSorteio.add(resultadoPanel, BorderLayout.CENTER);
        painelSorteio.add(sortearPanel, BorderLayout.NORTH);
        painelSorteio.add(vencedoresScroll, BorderLayout.EAST);

        return painelSorteio;
    }

    private JPanel criarPainelAposta() {
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

        JPanel cpfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cpfPanel.setBackground(Color.WHITE);
        cpfPanel.add(new JLabel("CPF do Apostador:"));
        JTextField cpfField = new JTextField(14);
        cpfPanel.add(cpfField);

        // Crie os componentes
        JComboBox<String> animalCombo = new JComboBox<>(mapaAnimalParaGrupo.keySet().stream().map(this::capitalize).toArray(String[]::new));
        JTextField dezenaField = new JTextField(4);
        JTextField milharField = new JTextField(5);

        // Painel para o campo dinâmico
        JPanel numeroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        numeroPanel.setBackground(Color.WHITE);
        numeroPanel.add(new JLabel("Número/Animal:"));
        numeroPanel.add(animalCombo); // Começa com animalCombo

        // Listener para trocar o campo conforme o tipo
        tipoCombo.addActionListener(e -> {
            numeroPanel.removeAll();
            numeroPanel.add(new JLabel("Número/Animal:"));
            String tipo = (String) tipoCombo.getSelectedItem();
            if ("Grupo".equalsIgnoreCase(tipo)) {
                numeroPanel.add(animalCombo);
            } else if ("Dezena".equalsIgnoreCase(tipo)) {
                dezenaField.setText("");
                numeroPanel.add(dezenaField);
            } else if ("Milhar".equalsIgnoreCase(tipo)) {
                milharField.setText("");
                numeroPanel.add(milharField);
            }
            numeroPanel.revalidate();
            numeroPanel.repaint();
        });

        JPanel valorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        valorPanel.setBackground(Color.WHITE);
        valorPanel.add(new JLabel("Valor: R$"));
        JTextField valorField = new JTextField(10);
        valorPanel.add(valorField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton apostarButton = new JButton("Fazer Aposta");
        JButton limparButton = new JButton("Limpar Apostas");
        buttonPanel.add(apostarButton);
        buttonPanel.add(limparButton);

        JPanel resultadoPanel = new JPanel(new BorderLayout());
        imagemApostaLabel = new JLabel();
        imagemApostaLabel.setHorizontalAlignment(JLabel.CENTER);
        resultadoPanel.add(imagemApostaLabel, BorderLayout.CENTER);

        controlPanel.add(tipoPanel);
        controlPanel.add(numeroPanel);
        controlPanel.add(valorPanel);
        controlPanel.add(cpfPanel);
        controlPanel.add(buttonPanel);

        listaApostasModel = new DefaultListModel<>();
        listaApostas = new JList<>(listaApostasModel);
        JScrollPane scrollApostas = new JScrollPane(listaApostas);
        scrollApostas.setBorder(BorderFactory.createTitledBorder("Apostas Realizadas"));

        apostarButton.addActionListener(e -> {
            try {
                String tipo = (String) tipoCombo.getSelectedItem();
                String numero;
                if ("Grupo".equalsIgnoreCase(tipo)) {
                    numero = animalCombo.getSelectedItem().toString().toLowerCase();
                } else if ("Dezena".equalsIgnoreCase(tipo)) {
                    numero = dezenaField.getText().trim();
                    int dezena = Integer.parseInt(numero);
                    if (dezena < 0 || dezena > 99) throw new NumberFormatException();
                } else {
                    numero = milharField.getText().trim();
                    int milhar = Integer.parseInt(numero);
                    if (milhar < 0 || milhar > 9999) throw new NumberFormatException();
                }
                String valorStr = valorField.getText().trim();
                String cpf = cpfField.getText().trim();

                if (numero.isEmpty() || valorStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.", "Atenção", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double valor = Double.parseDouble(valorStr);
                Aposta aposta = apostaController.criarAposta(tipo, numero, valor,cpf);
                apostas.add(aposta);
                listaApostasModel.addElement(aposta.toString());

                    String nomeAnimal = normalizarNomeAnimal(numero);
        ImageIcon imagem = animaisImagens.getOrDefault(nomeAnimal, criarIconePlaceholder(nomeAnimal));
        imagemApostaLabel.setIcon(imagem);

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Digite um número válido para o tipo selecionado.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        limparButton.addActionListener(e -> {
            apostas.clear();
            listaApostasModel.clear();
        });

        painelPrincipal.add(controlPanel);
        painelPrincipal.add(resultadoPanel);
        painelPrincipal.add(scrollApostas);

        return painelPrincipal;
    }

    private JPanel criarPainelGrupos() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);

        String[] colunas = {"Grupo", "Animal", "Dezenas"};
        String[][] dados = new String[25][3];

        String[] nomes = {
            "Avestruz", "Águia", "Burro", "Borboleta", "Cachorro",
            "Cabra", "Carneiro", "Camelo", "Cobra", "Coelho",
            "Cavalo", "Elefante", "Galo", "Gato", "Jacaré",
            "Leão", "Macaco", "Porco", "Pavão", "Peru",
            "Touro", "Tigre", "Urso", "Veado", "Vaca"
        };

        for (int i = 0; i < 25; i++) {
            int dezenaInicial = i * 4 + 1;
            int dezenaFinal = dezenaInicial + 3;
            String dezenas = String.format("%02d a %02d", dezenaInicial, dezenaFinal);
            dados[i][0] = String.valueOf(i + 1);
            dados[i][1] = nomes[i];
            dados[i][2] = dezenas;
        }

        javax.swing.JTable tabela = new javax.swing.JTable(dados, colunas);
        tabela.setEnabled(false);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tabela.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(tabela);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private void initUI() {
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Fazer Aposta", criarPainelAposta());
        abas.addTab("Realizar Sorteio", criarPainelSorteio());
        abas.addTab("Grupos e Dezenas", criarPainelGrupos()); // <-- nova aba
        add(abas, BorderLayout.CENTER);
    }

    @Override
    public void update(Resultado resultado) {
        StringBuilder vencedores = new StringBuilder();
        boolean houveVencedor = false;
        double totalPremios = 0.0;

        for (Aposta aposta : apostas) {
            String tipo = aposta.getTipoAposta();
            String valorApostado = aposta.getNumeroAposta().trim().toLowerCase();
            boolean venceu = false;

            try {
                switch (tipo.toLowerCase()) {
                    case "milhar":
                        venceu = Integer.parseInt(valorApostado) == resultado.getMilhar();
                        break;
                    case "dezena":
                        venceu = Integer.parseInt(valorApostado) == resultado.getDezena();
                        break;
                    case "grupo":
                        int grupoResultado = (resultado.getDezena() == 100) ? 25 : ((resultado.getDezena() - 1) / 4 + 1);
                        Integer grupoAposta = null;
                        try {
                            grupoAposta = Integer.parseInt(valorApostado);
                        } catch (NumberFormatException e) {
                            grupoAposta = mapaAnimalParaGrupo.get(valorApostado);
                        }
                        venceu = grupoAposta != null && grupoAposta == grupoResultado;
                        break;
                }
            } catch (Exception e) {
                venceu = false;
            }

            if (venceu) {
                houveVencedor = true;
                vencedores.append(aposta.toString()).append("\n");
                totalPremios += aposta.calcularPremio();
            }
        }

        String mensagem = String.format(
            "<html><center>Resultado:<br>Milhar: %s<br>Dezena: %s<br>Animal: %s<br>Prêmio: R$%.2f</center></html>",
            resultado.getMilharFormatado(),
            resultado.getDezenaFormatada(),
            resultado.getAnimal(),
            totalPremios
        );
        resultadoLabel.setText(mensagem);
        resultadoLabel.setIcon(null);
        imagemResultadoLabel.setIcon(animaisImagens.get(normalizarNomeAnimal(resultado.getAnimal())));

        if (houveVencedor) {
            JOptionPane.showMessageDialog(this, "Parabéns! Uma das apostas foi vencedora!", "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma aposta foi vencedora.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
        }

        if (vencedoresTextArea != null) {
            String texto = houveVencedor ? vencedores.toString() : "Nenhuma aposta vencedora.";
            if (houveVencedor) {
                texto += "\nTOTAL DE PRÊMIOS PAGOS: R$" + String.format("%.2f", totalPremios);
            }
            vencedoresTextArea.setText(texto);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JogoDoBichoGUI jogo = new JogoDoBichoGUI();
            jogo.setVisible(true);
        });
    }
}
