import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SistemaMedicamentos {

    private static Map<String, Medicamento> medicamentos = new HashMap<>();
    private static Set<String> laboratorios = new HashSet<>();
    private static Set<String> principiosAtivos = new HashSet<>();
    private static Set<String> paraQueServe = new HashSet<>();
    private static Set<String> posologias = new HashSet<>();

    public static void main(String[] args) {
        carregarDados();
        SwingUtilities.invokeLater(SistemaMedicamentos::criarMenuPrincipal);
    }

    // Classe para representar um medicamento
    private static class Medicamento {
        String descricao;
        String laboratorio;
        String paraQueServe;
        String posologia;
        String principioAtivo;

        Medicamento(String descricao, String laboratorio, String paraQueServe, String posologia, String principioAtivo) {
            this.descricao = descricao;
            this.laboratorio = laboratorio;
            this.paraQueServe = paraQueServe;
            this.posologia = posologia;
            this.principioAtivo = principioAtivo;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }

    // Menu principal
    private static void criarMenuPrincipal() {
        JFrame frame = new JFrame("Sistema de Medicamentos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Painel com imagem de fundo
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("C:\\Users\\Usuario\\Desktop\\cia dos animais\\untitled\\imagem do krl, funcionaaa.jpg"); // Coloque o caminho da sua imagem aqui
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };

        JLabel titulo = new JLabel("Cia dos Animais", JLabel.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 48)); // Fonte maior e mais bonita
        titulo.setForeground(Color.WHITE); // Texto branco
        panel.add(titulo, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        botoes.setOpaque(false); // Painel transparente
        botoes.setLayout(new GridLayout(3, 1, 20, 20));

        JButton btnCadastro = new JButton("Cadastro de Medicamentos");
        btnCadastro.setFont(new Font("SansSerif", Font.BOLD, 24)); // Fonte maior
        btnCadastro.setBackground(new Color(50, 205, 50)); // Verde claro
        btnCadastro.setForeground(Color.WHITE); // Texto branco
        btnCadastro.addActionListener(e -> verificarSenha());

        JButton btnBusca = new JButton("Buscar Produtos");
        btnBusca.setFont(new Font("SansSerif", Font.BOLD, 24)); // Fonte maior
        btnBusca.setBackground(new Color(50, 205, 50)); // Verde claro
        btnBusca.setForeground(Color.WHITE); // Texto branco
        btnBusca.addActionListener(e -> abrirTelaBusca());

        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("SansSerif", Font.BOLD, 24)); // Fonte maior
        btnSair.setBackground(new Color(50, 205, 50)); // Verde claro
        btnSair.setForeground(Color.WHITE); // Texto branco
        btnSair.addActionListener(e -> {
            salvarDados();
            System.exit(0);
        });

        botoes.add(btnCadastro);
        botoes.add(btnBusca);
        botoes.add(btnSair);

        panel.add(botoes, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Verificação de senha para acessar o cadastro
    private static void verificarSenha() {
        String senhaCorreta = "admin123"; // Senha fixa
        String senha = JOptionPane.showInputDialog("Digite a senha para acessar o cadastro:");
        if (senha != null && senha.equals(senhaCorreta)) {
            abrirTelaCadastro();
        } else {
            JOptionPane.showMessageDialog(null, "Senha incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tela de cadastro
    private static void abrirTelaCadastro() {
        JFrame cadastroFrame = new JFrame("Cadastro de Medicamentos");
        cadastroFrame.setSize(800, 600);

        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("\"C:\\Users\\Usuario\\Desktop\\cia dos animais\\untitled\\imagem do krl, funcionaaa.jpg"); // Coloque o caminho da sua imagem aqui
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setOpaque(false); // Painel transparente
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblDescricao = new JLabel("Descrição do Produto:");
        lblDescricao.setFont(new Font("SansSerif", Font.BOLD, 18)); // Fonte maior
        lblDescricao.setForeground(Color.WHITE); // Texto branco
        JTextField txtDescricao = new JTextField(20);
        txtDescricao.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Fonte maior

        JLabel lblLaboratorio = new JLabel("Laboratório:");
        lblLaboratorio.setFont(new Font("SansSerif", Font.BOLD, 18)); // Fonte maior
        lblLaboratorio.setForeground(Color.WHITE); // Texto branco
        JComboBox<String> comboLaboratorio = new JComboBox<>(laboratorios.toArray(new String[0]));
        comboLaboratorio.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Fonte maior
        comboLaboratorio.setBackground(new Color(50, 205, 50)); // Verde claro
        comboLaboratorio.setForeground(Color.WHITE); // Texto branco

        JButton btnAddLaboratorio = new JButton("+ Adicionar");
        btnAddLaboratorio.setFont(new Font("SansSerif", Font.BOLD, 16)); // Fonte maior
        btnAddLaboratorio.setBackground(new Color(50, 205, 50)); // Verde claro
        btnAddLaboratorio.setForeground(Color.WHITE); // Texto branco
        btnAddLaboratorio.addActionListener(e -> adicionarItem("Laboratório", laboratorios, comboLaboratorio));

        JLabel lblParaQueServe = new JLabel("Para que serve:");
        lblParaQueServe.setFont(new Font("SansSerif", Font.BOLD, 18)); // Fonte maior
        lblParaQueServe.setForeground(Color.WHITE); // Texto branco
        JComboBox<String> comboParaQueServe = new JComboBox<>(paraQueServe.toArray(new String[0]));
        comboParaQueServe.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Fonte maior
        comboParaQueServe.setBackground(new Color(50, 205, 50)); // Verde claro
        comboParaQueServe.setForeground(Color.WHITE); // Texto branco

        JButton btnAddParaQueServe = new JButton("+ Adicionar");
        btnAddParaQueServe.setFont(new Font("SansSerif", Font.BOLD, 16)); // Fonte maior
        btnAddParaQueServe.setBackground(new Color(50, 205, 50)); // Verde claro
        btnAddParaQueServe.setForeground(Color.WHITE); // Texto branco
        btnAddParaQueServe.addActionListener(e -> adicionarItem("Para que serve", paraQueServe, comboParaQueServe));

        JLabel lblPosologia = new JLabel("Posologia:");
        lblPosologia.setFont(new Font("SansSerif", Font.BOLD, 18)); // Fonte maior
        lblPosologia.setForeground(Color.WHITE); // Texto branco
        JComboBox<String> comboPosologia = new JComboBox<>(posologias.toArray(new String[0]));
        comboPosologia.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Fonte maior
        comboPosologia.setBackground(new Color(50, 205, 50)); // Verde claro
        comboPosologia.setForeground(Color.WHITE); // Texto branco

        JButton btnAddPosologia = new JButton("+ Adicionar");
        btnAddPosologia.setFont(new Font("SansSerif", Font.BOLD, 16)); // Fonte maior
        btnAddPosologia.setBackground(new Color(50, 205, 50)); // Verde claro
        btnAddPosologia.setForeground(Color.WHITE); // Texto branco
        btnAddPosologia.addActionListener(e -> adicionarItem("Posologia", posologias, comboPosologia));

        JLabel lblPrincipioAtivo = new JLabel("Princípio Ativo:");
        lblPrincipioAtivo.setFont(new Font("SansSerif", Font.BOLD, 18)); // Fonte maior
        lblPrincipioAtivo.setForeground(Color.WHITE); // Texto branco
        JComboBox<String> comboPrincipioAtivo = new JComboBox<>(principiosAtivos.toArray(new String[0]));
        comboPrincipioAtivo.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Fonte maior
        comboPrincipioAtivo.setBackground(new Color(50, 205, 50)); // Verde claro
        comboPrincipioAtivo.setForeground(Color.WHITE); // Texto branco

        JButton btnAddPrincipioAtivo = new JButton("+ Adicionar");
        btnAddPrincipioAtivo.setFont(new Font("SansSerif", Font.BOLD, 16)); // Fonte maior
        btnAddPrincipioAtivo.setBackground(new Color(50, 205, 50)); // Verde claro
        btnAddPrincipioAtivo.setForeground(Color.WHITE); // Texto branco
        btnAddPrincipioAtivo.addActionListener(e -> adicionarItem("Princípio Ativo", principiosAtivos, comboPrincipioAtivo));

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("SansSerif", Font.BOLD, 18)); // Fonte maior
        btnSalvar.setBackground(new Color(50, 205, 50)); // Verde claro
        btnSalvar.setForeground(Color.WHITE); // Texto branco
        btnSalvar.addActionListener(e -> {
            String descricao = txtDescricao.getText().trim();
            String laboratorio = (String) comboLaboratorio.getSelectedItem();
            String paraQue = (String) comboParaQueServe.getSelectedItem();
            String posologia = (String) comboPosologia.getSelectedItem();
            String principioAtivo = (String) comboPrincipioAtivo.getSelectedItem();

            if (descricao.isEmpty() || medicamentos.containsKey(descricao)) {
                JOptionPane.showMessageDialog(cadastroFrame, "Descrição inválida ou já cadastrada.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                medicamentos.put(descricao, new Medicamento(descricao, laboratorio, paraQue, posologia, principioAtivo));
                salvarDados();
                JOptionPane.showMessageDialog(cadastroFrame, "Medicamento salvo com sucesso!");
                cadastroFrame.dispose();
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblDescricao, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(txtDescricao, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblLaboratorio, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(comboLaboratorio, gbc);
        gbc.gridx = 2; gbc.gridy = 1; panel.add(btnAddLaboratorio, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblParaQueServe, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(comboParaQueServe, gbc);
        gbc.gridx = 2; gbc.gridy = 2; panel.add(btnAddParaQueServe, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(lblPosologia, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(comboPosologia, gbc);
        gbc.gridx = 2; gbc.gridy = 3; panel.add(btnAddPosologia, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(lblPrincipioAtivo, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(comboPrincipioAtivo, gbc);
        gbc.gridx = 2; gbc.gridy = 4; panel.add(btnAddPrincipioAtivo, gbc);

        gbc.gridx = 1; gbc.gridy = 6; panel.add(btnSalvar, gbc);

        cadastroFrame.add(panel);
        cadastroFrame.setLocationRelativeTo(null);
        cadastroFrame.setVisible(true);
    }

    // Tela de busca
    private static void abrirTelaBusca() {
        JFrame buscaFrame = new JFrame("Buscar Produtos");
        buscaFrame.setSize(800, 600);

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("C:\\Users\\Usuario\\Desktop\\cia dos animais\\untitled\\imagem do krl, funcionaaa.jpg"); // Coloque o caminho da sua imagem aqui
                Image img = icon.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setOpaque(false);
        // Painel transparente

        JTextField txtBusca = new JTextField(20);
        txtBusca.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Fonte maior

        JComboBox<String> comboFiltro = new JComboBox<>(new String[]{"Descrição", "Laboratório", "Para que serve", "Posologia", "Princípio Ativo"});
        comboFiltro.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Fonte maior
        comboFiltro.setBackground(new Color(50, 205, 50)); // Verde claro
        comboFiltro.setForeground(Color.WHITE); // Texto branco

        JComboBox<String> comboEspecifico = new JComboBox<>();
        comboEspecifico.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Fonte maior
        comboEspecifico.setBackground(new Color(50, 205, 50)); // Verde claro
        comboEspecifico.setForeground(Color.blue); // Texto branco

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("SansSerif", Font.BOLD, 16)); // Fonte maior
        btnBuscar.setBackground(new Color(50, 205, 50)); // Verde claro
        btnBuscar.setForeground(Color.blue); // Texto branco

        JList<String> listaResultados = new JList<>();
        listaResultados.setFont(new Font("SansSerif", Font.PLAIN, 16));
        listaResultados.setOpaque(false); // Torna o JList transparente
        listaResultados.setForeground(Color.blue);

// Cria o JScrollPane transparente
        JScrollPane scrollPane = new JScrollPane(listaResultados);
        scrollPane.setOpaque(false); // ScrollPane transparente
        scrollPane.getViewport().setOpaque(false); // Viewport transparente

        comboFiltro.addActionListener(e -> {
            String filtroSelecionado = (String) comboFiltro.getSelectedItem();
            comboEspecifico.removeAllItems();
            switch (filtroSelecionado) {
                case "Laboratório":
                    laboratorios.forEach(comboEspecifico::addItem);
                    break;
                case "Para que serve":
                    paraQueServe.forEach(comboEspecifico::addItem);
                    break;
                case "Posologia":
                    posologias.forEach(comboEspecifico::addItem);
                    break;
                case "Princípio Ativo":
                    principiosAtivos.forEach(comboEspecifico::addItem);
                    break;
            }
        });

        btnBuscar.addActionListener(e -> {
            String termoBusca = txtBusca.getText().trim().toLowerCase();
            String filtro = (String) comboFiltro.getSelectedItem();
            String especificoSelecionado = (String) comboEspecifico.getSelectedItem();

            List<String> resultados = medicamentos.values().stream()
                    .filter(med -> {
                        switch (filtro) {
                            case "Descrição":
                                return med.descricao.toLowerCase().contains(termoBusca);
                            case "Laboratório":
                                return med.laboratorio.equals(especificoSelecionado);
                            case "Para que serve":
                                return med.paraQueServe.equals(especificoSelecionado);
                            case "Posologia":
                                return med.posologia.equals(especificoSelecionado);
                            case "Princípio Ativo":
                                return med.principioAtivo.equals(especificoSelecionado);
                            default:
                                return false;
                        }
                    })
                    .map(med -> med.descricao)
                    .sorted()
                    .collect(Collectors.toList());

            listaResultados.setListData(resultados.toArray(new String[0]));
        });

        listaResultados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selecionado = listaResultados.getSelectedValue();
                if (selecionado != null) {
                    Medicamento med = medicamentos.get(selecionado);
                    JOptionPane.showMessageDialog(buscaFrame,
                            "Descrição: " + med.descricao + "\n" +
                                    "Laboratório: " + med.laboratorio + "\n" +
                                    "Para que serve: " + med.paraQueServe + "\n" +
                                    "Posologia: " + med.posologia + "\n" +
                                    "Princípio Ativo: " + med.principioAtivo,
                            "Detalhes do Produto", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false); // Painel transparente
        topPanel.add(new JLabel("Buscar por:"));
        topPanel.add(comboFiltro);
        topPanel.add(comboEspecifico);
        topPanel.add(txtBusca);
        topPanel.add(btnBuscar);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        buscaFrame.add(panel);
        buscaFrame.setLocationRelativeTo(null);
        buscaFrame.setVisible(true);
    }

    // Método para adicionar itens às listas
    private static void adicionarItem(String tipo, Set<String> lista, JComboBox<String> comboBox) {
        String novoItem = JOptionPane.showInputDialog("Adicionar " + tipo + ":");
        if (novoItem != null && !novoItem.trim().isEmpty() && !lista.contains(novoItem.trim())) {
            lista.add(novoItem.trim());
            comboBox.addItem(novoItem.trim());
            salvarDados();
        }
    }

    // Persistência de dados
    private static void salvarDados() {
        salvarSet(laboratorios, "laboratorios.txt");
        salvarSet(principiosAtivos, "principiosAtivos.txt");
        salvarSet(paraQueServe, "paraQueServe.txt");
        salvarSet(posologias, "posologias.txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter("medicamentos.txt"))) {
            for (Medicamento med : medicamentos.values()) {
                writer.println(med.descricao + ";" + med.laboratorio + ";" + med.paraQueServe + ";" + med.posologia + ";" + med.principioAtivo);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void carregarDados() {
        laboratorios = carregarSet("laboratorios.txt");
        principiosAtivos = carregarSet("principiosAtivos.txt");
        paraQueServe = carregarSet("paraQueServe.txt");
        posologias = carregarSet("posologias.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader("medicamentos.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 5) {
                    medicamentos.put(dados[0], new Medicamento(dados[0], dados[1], dados[2], dados[3], dados[4]));
                }
            }
        } catch (IOException ignored) {
        }
    }

    private static Set<String> carregarSet(String arquivo) {
        Set<String> set = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                set.add(linha);
            }
        } catch (IOException ignored) {
        }
        return set;
    }

    private static void salvarSet(Set<String> set, String arquivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo))) {
            for (String item : set) {
                writer.println(item);
            }
        } catch (IOException ignored) {
        }
    }
}