package com.example.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.Objects;

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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.example.controller.ApostaController;
import com.example.model.Aposta;
import com.example.model.Resultado;
import com.example.observer.ApostadorObserver;
import com.example.observer.SorteioObservable;
import com.example.repository.AnimalRepository;
import com.example.repository.ApostaRepository;

public class JogoDoBichoGUI extends JFrame implements ApostadorObserver {

    private ApostaController apostaController;
    private AnimalRepository animalRepository;

    private JLabel resultadoLabel;
    private JLabel imagemApostaLabel;
    private JLabel imagemResultadoLabel;
    private DefaultListModel<String> listaApostasModel;
    private JList<String> listaApostas;
    private JTextArea vencedoresTextArea;

    public JogoDoBichoGUI(ApostaController apostaController, AnimalRepository animalRepository) {
        super("Jogo do Bicho");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        this.apostaController = apostaController;
        this.animalRepository = animalRepository;

        // Registra GUI como Observer
        SorteioObservable sorteio = apostaController.getSorteioObservable();
        sorteio.addObserver(this);

        initUI();
    }

    private JPanel criarPainelAposta() {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelPrincipal.setBackground(Color.WHITE);

        JPanel controlPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        controlPanel.setBackground(Color.WHITE);

        // Tipo de aposta
        JPanel tipoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tipoPanel.setBackground(Color.WHITE);
        tipoPanel.add(new JLabel("Tipo de aposta:"));
        String[] tipos = {"Grupo", "Dezena", "Milhar"};
        JComboBox<String> tipoCombo = new JComboBox<>(tipos);
        tipoPanel.add(tipoCombo);

        // CPF
        JPanel cpfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cpfPanel.setBackground(Color.WHITE);
        cpfPanel.add(new JLabel("CPF do Apostador:"));
        JTextField cpfField = new JTextField(14);
        cpfPanel.add(cpfField);

        // Componentes de aposta
        JComboBox<String> animalCombo = new JComboBox<>(animalRepository.getTodosAnimais());
        JTextField dezenaField = new JTextField(4);
        JTextField milharField = new JTextField(5);

        // Painel dinâmico de número/animal
        JPanel numeroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        numeroPanel.setBackground(Color.WHITE);
        numeroPanel.add(new JLabel("Número/Animal:"));
        numeroPanel.add(animalCombo);

        tipoCombo.addActionListener(e -> {
            numeroPanel.removeAll();
            numeroPanel.add(new JLabel("Número/Animal:"));
            String tipo = (String) tipoCombo.getSelectedItem();
            switch (Objects.requireNonNull(tipo).toLowerCase()) {
                case "grupo" -> numeroPanel.add(animalCombo);
                case "dezena" -> {
                    dezenaField.setText("");
                    numeroPanel.add(dezenaField);
                }
                case "milhar" -> {
                    milharField.setText("");
                    numeroPanel.add(milharField);
                }
            }
            numeroPanel.revalidate();
            numeroPanel.repaint();
        });

        // Valor da aposta
        JPanel valorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        valorPanel.setBackground(Color.WHITE);
        valorPanel.add(new JLabel("Valor: R$"));
        JTextField valorField = new JTextField(10);
        valorPanel.add(valorField);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        JButton apostarButton = new JButton("Fazer Aposta");
        JButton limparButton = new JButton("Limpar Apostas");
        buttonPanel.add(apostarButton);
        buttonPanel.add(limparButton);

        // Painel da imagem da aposta
        JPanel resultadoPanel = new JPanel(new BorderLayout());
        imagemApostaLabel = new JLabel();
        imagemApostaLabel.setHorizontalAlignment(JLabel.CENTER);
        resultadoPanel.add(imagemApostaLabel, BorderLayout.CENTER);

        // Montagem do painel
        controlPanel.add(tipoPanel);
        controlPanel.add(numeroPanel);
        controlPanel.add(valorPanel);
        controlPanel.add(cpfPanel);
        controlPanel.add(buttonPanel);

        listaApostasModel = new DefaultListModel<>();
        listaApostas = new JList<>(listaApostasModel);
        JScrollPane scrollApostas = new JScrollPane(listaApostas);
        scrollApostas.setBorder(BorderFactory.createTitledBorder("Apostas Realizadas"));

        // Ações dos botões
        apostarButton.addActionListener(e -> {
            try {
                String tipo = (String) tipoCombo.getSelectedItem();
                String numero;
                if ("Grupo".equalsIgnoreCase(tipo)) {
                    numero = Objects.requireNonNull(animalCombo.getSelectedItem()).toString().toLowerCase();
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

                // **Chama o Controller para criar e registrar aposta**
                apostaController.criarERegistrarAposta(tipo, numero, valor, cpf);

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Digite um número válido para o tipo selecionado.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        limparButton.addActionListener(e -> {
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

        String[] animais = animalRepository.getTodosAnimais();
        for (int i = 0; i < 25; i++) {
            dados[i][0] = String.valueOf(animalRepository.getGrupo(animais[i]));
            dados[i][1] = animais[i];
            dados[i][2] = animalRepository.getDezenas(i + 1);
        }

        JTable tabela = new JTable(dados, colunas);
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
        abas.addTab("Grupos e Dezenas", criarPainelGrupos());
        add(abas, BorderLayout.CENTER);
    }

    private JPanel criarPainelSorteio() {
    JPanel painel = new JPanel(new BorderLayout(10, 10));
    painel.setBackground(Color.WHITE);

    JPanel resultadoPanel = new JPanel(new BorderLayout());
    resultadoPanel.setBorder(BorderFactory.createTitledBorder("Resultado"));
    resultadoPanel.setBackground(Color.WHITE);
    resultadoLabel = new JLabel("Aguardando sorteio...", JLabel.CENTER);
    resultadoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
    resultadoPanel.add(resultadoLabel, BorderLayout.NORTH);

    imagemResultadoLabel = new JLabel();
    imagemResultadoLabel.setHorizontalAlignment(JLabel.CENTER);
    resultadoPanel.add(imagemResultadoLabel, BorderLayout.CENTER);

    // Painel para os botões de sorteio
    JPanel sortearPanel = new JPanel(new FlowLayout());
    sortearPanel.setBackground(Color.WHITE);
    
    // Botão de sorteio normal
    JButton sortearButton = new JButton("Realizar Sorteio Aleatório");
    sortearButton.addActionListener(e -> apostaController.realizarSorteio());
    
    
    
    sortearPanel.add(sortearButton);

    vencedoresTextArea = new JTextArea();
    vencedoresTextArea.setEditable(false);
    vencedoresTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    vencedoresTextArea.setBorder(BorderFactory.createTitledBorder("Apostas Vencedoras"));
    JScrollPane scroll = new JScrollPane(vencedoresTextArea);

    painel.add(resultadoPanel, BorderLayout.CENTER);
    painel.add(sortearPanel, BorderLayout.NORTH);
    painel.add(scroll, BorderLayout.EAST);

    return painel;
}

    @Override
public void update(Resultado resultado) {
    // Exibe o resultado
    resultadoLabel.setText(String.format(
            "<html><center>Resultado:<br>Milhar: %04d<br>Animal: %s<br>Prêmio total: R$%.2f</center></html>",
            resultado.getNumeroSorteado(),
            resultado.getAnimal(),
            resultado.getPremioTotal()  // ← Correto!
    ));
    imagemResultadoLabel.setIcon(animalRepository.getImagem(resultado.getAnimal()));

    List<Aposta> ganhadoras = resultado.getGanhadoras(); // ← PERFEITO!
    if (ganhadoras.isEmpty()) {
        vencedoresTextArea.setText("Nenhuma aposta vencedora.");
        JOptionPane.showMessageDialog(this, "Nenhuma aposta foi vencedora.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
    } else {
        StringBuilder sb = new StringBuilder();
        for (Aposta a : ganhadoras) {
            sb.append(a.toString()).append("\n");
        }
        sb.append("\nTOTAL DE PRÊMIOS PAGOS: R$").append(resultado.getPremioTotal()); // ← Correto!
        vencedoresTextArea.setText(sb.toString());
        JOptionPane.showMessageDialog(this, "Parabéns! Algumas apostas foram vencedoras!", "Resultado", JOptionPane.INFORMATION_MESSAGE);
    }
}

    @Override
    public void novaAposta(Aposta aposta) {
        listaApostasModel.addElement(aposta.toString());
        ImageIcon img = animalRepository.getImagem(aposta.getNumeroAposta());
        imagemApostaLabel.setIcon(img);
    }

    


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Cria repositórios
            ApostaRepository apostaRepository = new ApostaRepository();
            AnimalRepository animalRepository = new AnimalRepository();

            // Cria Observable e Controller
            SorteioObservable sorteioObservable = new SorteioObservable();
            ApostaController apostaController = new ApostaController(sorteioObservable, apostaRepository);

            // Cria e exibe a GUI passando os objetos
            JogoDoBichoGUI gui = new JogoDoBichoGUI(apostaController, animalRepository);
            gui.setVisible(true);
        });
    }
}