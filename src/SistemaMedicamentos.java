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

    public static void main(String[] args) {
        carregarDados();
        SwingUtilities.invokeLater(SistemaMedicamentos::criarMenuPrincipal);
    }

    private static class Medicamento {
        String descricao;
        String laboratorio;
        String paraQueServe;
        String principioAtivo;

        Medicamento(String descricao, String laboratorio, String paraQueServe, String principioAtivo) {
            this.descricao = descricao;
            this.laboratorio = laboratorio;
            this.paraQueServe = paraQueServe;
            this.principioAtivo = principioAtivo;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }

    private static void criarMenuPrincipal() {
        JFrame frame = new JFrame("Sistema de Medicamentos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new BorderLayout());

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

    private static void abrirTelaCadastro() {
    }

    private static void abrirTelaBusca() {
        JFrame buscaFrame = new JFrame("Buscar Produtos");
        buscaFrame.setSize(600, 400);

        JPanel panel = new JPanel(new BorderLayout());

        JTextField txtBusca = new JTextField(20);
        JComboBox<String> comboFiltro = new JComboBox<>(new String[]{"Descrição", "Laboratório", "Para que serve", "Princípio Ativo"});
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
                case "Princípio Ativo":
                    principiosAtivos.forEach(comboEspecifico::addItem);
                    break;
            }
            comboEspecifico.setSelectedIndex(-1);
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

        panel.add(new JLabel("Buscar por:"), BorderLayout.NORTH);
        panel.add(comboFiltro, BorderLayout.WEST);
        panel.add(comboEspecifico, BorderLayout.CENTER);
        panel.add(txtBusca, BorderLayout.EAST);
        panel.add(btnBuscar, BorderLayout.SOUTH);
        panel.add(new JScrollPane(listaResultados), BorderLayout.CENTER);

        buscaFrame.add(panel);
        buscaFrame.setLocationRelativeTo(null);
        buscaFrame.setVisible(true);
    }

    private static void salvarDados() {
        salvarSet(laboratorios, "laboratorios.txt");
        salvarSet(principiosAtivos, "principiosAtivos.txt");
        salvarSet(paraQueServe, "paraQueServe.txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter("medicamentos.txt"))) {
            for (Medicamento med : medicamentos.values()) {
                writer.println(med.descricao + ";" + med.laboratorio + ";" + med.paraQueServe + ";" + med.principioAtivo);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void carregarDados() {
        laboratorios = carregarSet("laboratorios.txt");
        principiosAtivos = carregarSet("principiosAtivos.txt");
        paraQueServe = carregarSet("paraQueServe.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader("medicamentos.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 4) {
                    medicamentos.put(dados[0], new Medicamento(dados[0], dados[1], dados[2], dados[3]));
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