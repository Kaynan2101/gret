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
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(204, 255, 204));

        JLabel titulo = new JLabel("Cia dos Animais", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel botoes = new JPanel();
        botoes.setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnCadastro = new JButton("Cadastro de Medicamentos");
        btnCadastro.addActionListener(e -> abrirTelaCadastro());

        JButton btnBusca = new JButton("Buscar Produtos");
        btnBusca.addActionListener(e -> abrirTelaBusca());

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> {
            salvarDados();
            System.exit(0);
        });

        botoes.add(btnCadastro);
        botoes.add(btnBusca);
        botoes.add(btnSair);

        panel.add(botoes, BorderLayout.CENTER);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Tela de cadastro
    private static void abrirTelaCadastro() {
        JFrame cadastroFrame = new JFrame("Cadastro de Medicamentos");
        cadastroFrame.setSize(600, 400);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(204, 255, 204));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblDescricao = new JLabel("Descrição do Produto:");
        JTextField txtDescricao = new JTextField(20);

        JLabel lblLaboratorio = new JLabel("Laboratório:");
        JComboBox<String> comboLaboratorio = new JComboBox<>(laboratorios.toArray(new String[0]));
        JButton btnAddLaboratorio = new JButton("+ Adicionar");
        btnAddLaboratorio.addActionListener(e -> adicionarItem("Laboratório", laboratorios, comboLaboratorio));

        JLabel lblParaQueServe = new JLabel("Para que serve:");
        JComboBox<String> comboParaQueServe = new JComboBox<>(paraQueServe.toArray(new String[0]));
        JButton btnAddParaQueServe = new JButton("+ Adicionar");
        btnAddParaQueServe.addActionListener(e -> adicionarItem("Para que serve", paraQueServe, comboParaQueServe));

        JLabel lblPosologia = new JLabel("Posologia:");
        JComboBox<String> comboPosologia = new JComboBox<>(posologias.toArray(new String[0]));
        JButton btnAddPosologia = new JButton("+ Adicionar");
        btnAddPosologia.addActionListener(e -> adicionarItem("Posologia", posologias, comboPosologia));

        JLabel lblPrincipioAtivo = new JLabel("Princípio Ativo:");
        JComboBox<String> comboPrincipioAtivo = new JComboBox<>(principiosAtivos.toArray(new String[0]));
        JButton btnAddPrincipioAtivo = new JButton("+ Adicionar");
        btnAddPrincipioAtivo.addActionListener(e -> adicionarItem("Princípio Ativo", principiosAtivos, comboPrincipioAtivo));

        JButton btnSalvar = new JButton("Salvar");
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
        buscaFrame.setSize(600, 400);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(204, 255, 204));

        JTextField txtBusca = new JTextField(20);
        JComboBox<String> comboFiltro = new JComboBox<>(new String[]{"Descrição", "Laboratório", "Para que serve", "Posologia", "Princípio Ativo"});
        JComboBox<String> comboEspecifico = new JComboBox<>();

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

        JButton btnBuscar = new JButton("Buscar");
        JList<String> listaResultados = new JList<>();

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
        topPanel.add(new JLabel("Buscar por:"));
        topPanel.add(comboFiltro);
        topPanel.add(comboEspecifico);
        topPanel.add(txtBusca);
        topPanel.add(btnBuscar);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaResultados), BorderLayout.CENTER);

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