package aplicacaofinancas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AplicacaoControleFinanceiro {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GerenciadorFinanceiro gerenciador = new GerenciadorFinanceiro();
            new TelaPrincipal(gerenciador).setVisible(true);
        });
    }
}

// Classe principal de gerenciamento de dados
class GerenciadorFinanceiro {
    private double saldo;
    private List<Transacao> transacoes;
    
    public GerenciadorFinanceiro() {
        this.saldo = 0.0;
        this.transacoes = new ArrayList<>();
        // Adiciona dados de exemplo para demonstração
        adicionarDadosExemplo();
    }
    
    private void adicionarDadosExemplo() {
        adicionarTransacao(new Transacao("Depósito Inicial", 1500.00, "RECEITA", new Date()));
        adicionarTransacao(new Transacao("Supermercado", -125.75, "ALIMENTACAO", new Date()));
        adicionarTransacao(new Transacao("Trabalho Freelance", 500.00, "RECEITA", new Date()));
        adicionarTransacao(new Transacao("Conta de Luz", -89.90, "UTILIDADES", new Date()));
    }
    
    public void adicionarTransacao(Transacao t) {
        transacoes.add(t);
        saldo += t.getValor();
    }
    
    public double getSaldo() { return saldo; }
    public List<Transacao> getTransacoes() { return transacoes; }
    
    public double getTotalReceitas() {
        return transacoes.stream()
            .filter(t -> t.getValor() > 0)
            .mapToDouble(Transacao::getValor)
            .sum();
    }
    
    public double getTotalDespesas() {
        return transacoes.stream()
            .filter(t -> t.getValor() < 0)
            .mapToDouble(Transacao::getValor)
            .sum();
    }
}

// Classe que representa uma transação
class Transacao {
    private String descricao;
    private double valor;
    private String categoria;
    private Date data;
    
    public Transacao(String descricao, double valor, String categoria, Date data) {
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.data = data;
    }
    
    // Métodos de acesso (getters)
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
    public String getCategoria() { return categoria; }
    public Date getData() { return data; }
    
    public String getDataFormatada() {
        return new SimpleDateFormat("dd/MM/yyyy").format(data);
    }
    
    public String getTipo() {
        return valor >= 0 ? "Receita" : "Despesa";
    }
}

// Janela principal da aplicação
class TelaPrincipal extends JFrame {
    private GerenciadorFinanceiro gerenciador;
    private JLabel rotuloSaldo;
    private JTextArea areaExtrato;
    private JTable tabelaTransacoes;
    private ModeloTabelaTransacoes modeloTabela;
    
    public TelaPrincipal(GerenciadorFinanceiro gerenciador) {
        this.gerenciador = gerenciador;
        configurarInterface();
    }
    
    private void configurarInterface() {
        setTitle("Sistema de Controle Financeiro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Cria painel superior com saldo
        add(criarPainelSaldo(), BorderLayout.NORTH);
        
        // Cria painel central com abas
        add(criarPainelComAbas(), BorderLayout.CENTER);
        
        // Cria painel inferior com resumo
        add(criarPainelResumo(), BorderLayout.SOUTH);
        
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    private JPanel criarPainelSaldo() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painel.setBackground(new Color(70, 130, 180));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        rotuloSaldo = new JLabel();
        atualizarExibicaoSaldo();
        rotuloSaldo.setFont(new Font("SansSerif", Font.BOLD, 28));
        rotuloSaldo.setForeground(Color.WHITE);
        
        painel.add(rotuloSaldo);
        return painel;
    }
    
    private JTabbedPane criarPainelComAbas() {
        JTabbedPane painelAbas = new JTabbedPane();
        
        // Aba 1: Registrar Transação
        painelAbas.addTab("Registrar Transação", criarPainelTransacao());
        
        // Aba 2: Extrato Financeiro
        painelAbas.addTab("Extrato Financeiro", criarPainelExtrato());
        
        // Aba 3: Lista de Transações
        painelAbas.addTab("Lista de Transações", criarPainelListaTransacoes());
        
        return painelAbas;
    }
    
    private JPanel criarPainelTransacao() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        JTextField campoDescricao = new JTextField(20);
        painel.add(campoDescricao, gbc);
        
        // Valor
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Valor (R$):"), gbc);
        gbc.gridx = 1;
        JTextField campoValor = new JTextField(10);
        painel.add(campoValor, gbc);
        
        // Categoria
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1;
        String[] categorias = {"RECEITA", "ALIMENTACAO", "UTILIDADES", "TRANSPORTE", "LAZER", "OUTROS"};
        JComboBox<String> comboCategoria = new JComboBox<>(categorias);
        painel.add(comboCategoria, gbc);
        
        // Tipo (Receita/Despesa)
        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        ButtonGroup grupoTipo = new ButtonGroup();
        JRadioButton botaoReceita = new JRadioButton("Receita");
        JRadioButton botaoDespesa = new JRadioButton("Despesa", true);
        grupoTipo.add(botaoReceita);
        grupoTipo.add(botaoDespesa);
        
        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTipo.add(botaoReceita);
        painelTipo.add(botaoDespesa);
        painel.add(painelTipo, gbc);
        
        // Botão Adicionar Transação
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton botaoAdicionar = new JButton("Adicionar Transação");
        botaoAdicionar.addActionListener(e -> {
            try {
                String descricao = campoDescricao.getText();
                double valor = Double.parseDouble(campoValor.getText());
                String categoria = (String) comboCategoria.getSelectedItem();
                
                if (botaoDespesa.isSelected()) {
                    valor = -Math.abs(valor);
                }
                
                Transacao t = new Transacao(descricao, valor, categoria, new Date());
                gerenciador.adicionarTransacao(t);
                modeloTabela.adicionarTransacao(t);
                
                atualizarExibicaoSaldo();
                atualizarExibicaoExtrato();
                
                campoDescricao.setText("");
                campoValor.setText("");
                JOptionPane.showMessageDialog(this, "Transação adicionada com sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um valor válido!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        painel.add(botaoAdicionar, gbc);
        
        return painel;
    }
    
    private JPanel criarPainelExtrato() {
        JPanel painel = new JPanel(new BorderLayout());
        
        areaExtrato = new JTextArea(15, 50);
        areaExtrato.setEditable(false);
        areaExtrato.setFont(new Font("Monospaced", Font.PLAIN, 12));
        atualizarExibicaoExtrato();
        
        painel.add(new JScrollPane(areaExtrato), BorderLayout.CENTER);
        return painel;
    }
    
    private JPanel criarPainelListaTransacoes() {
        JPanel painel = new JPanel(new BorderLayout());
        
        modeloTabela = new ModeloTabelaTransacoes();
        for (Transacao t : gerenciador.getTransacoes()) {
            modeloTabela.adicionarTransacao(t);
        }
        
        tabelaTransacoes = new JTable(modeloTabela);
        tabelaTransacoes.setRowHeight(25);
        tabelaTransacoes.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabelaTransacoes.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabelaTransacoes.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabelaTransacoes.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        // Colorir despesas em vermelho
        tabelaTransacoes.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tabela, Object valor,
                    boolean selecionado, boolean comFoco, int linha, int coluna) {
                Component c = super.getTableCellRendererComponent(tabela, valor, 
                    selecionado, comFoco, linha, coluna);
                
                if (coluna == 1) { // Coluna Valor
                    double valorTransacao = (Double) valor;
                    if (valorTransacao < 0) {
                        c.setForeground(Color.RED);
                    } else {
                        c.setForeground(new Color(0, 128, 0)); // Verde escuro
                    }
                } else {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });
        
        painel.add(new JScrollPane(tabelaTransacoes), BorderLayout.CENTER);
        return painel;
    }
    
    private JPanel criarPainelResumo() {
        JPanel painel = new JPanel(new GridLayout(1, 3, 10, 0));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Total de Receitas
        JPanel painelReceitas = criarPainelMetrica("Total de Receitas", 
            String.format("R$ %.2f", gerenciador.getTotalReceitas()), 
            new Color(60, 179, 113));
        
        // Total de Despesas
        JPanel painelDespesas = criarPainelMetrica("Total de Despesas", 
            String.format("R$ %.2f", Math.abs(gerenciador.getTotalDespesas())), 
            new Color(220, 60, 60));
        
        // Fluxo Líquido
        double fluxoLiquido = gerenciador.getTotalReceitas() + gerenciador.getTotalDespesas();
        JPanel painelFluxo = criarPainelMetrica("Fluxo Líquido", 
            String.format("R$ %.2f", fluxoLiquido), 
            new Color(70, 130, 180));
        
        painel.add(painelReceitas);
        painel.add(painelDespesas);
        painel.add(painelFluxo);
        
        return painel;
    }
    
    private JPanel criarPainelMetrica(String titulo, String valor, Color cor) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cor.darker(), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel rotuloTitulo = new JLabel(titulo, SwingConstants.CENTER);
        rotuloTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JLabel rotuloValor = new JLabel(valor, SwingConstants.CENTER);
        rotuloValor.setFont(new Font("SansSerif", Font.BOLD, 16));
        rotuloValor.setForeground(cor.darker());
        
        painel.add(rotuloTitulo, BorderLayout.NORTH);
        painel.add(rotuloValor, BorderLayout.CENTER);
        
        return painel;
    }
    
    private void atualizarExibicaoSaldo() {
        double saldo = gerenciador.getSaldo();
        String textoSaldo = String.format("Saldo Atual: R$ %.2f", saldo);
        rotuloSaldo.setText(textoSaldo);
        
        // Atualizar cor baseada no saldo
        if (saldo >= 0) {
            rotuloSaldo.setForeground(new Color(0, 128, 0)); // Verde
        } else {
            rotuloSaldo.setForeground(Color.RED);
        }
    }
    
    private void atualizarExibicaoExtrato() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-30s %-12s %-15s %-10s\n", 
            "Data", "Descrição", "Categoria", "Valor"));
        sb.append("=".repeat(70)).append("\n");
        
        for (Transacao t : gerenciador.getTransacoes()) {
            sb.append(String.format("%-30s %-12s %-15s R$ %8.2f\n",
                t.getDataFormatada(),
                t.getDescricao().length() > 12 ? 
                    t.getDescricao().substring(0, 9) + "..." : t.getDescricao(),
                t.getCategoria(),
                t.getValor()));
        }
        
        sb.append("\n").append("=".repeat(70)).append("\n");
        sb.append(String.format("TOTAL: R$ %.2f", gerenciador.getSaldo()));
        
        areaExtrato.setText(sb.toString());
    }
}

// Modelo de tabela para lista de transações
class ModeloTabelaTransacoes extends javax.swing.table.AbstractTableModel {
    private List<Transacao> transacoes;
    private String[] colunas = {"Descrição", "Valor", "Categoria", "Data", "Tipo"};
    
    public ModeloTabelaTransacoes() {
        transacoes = new ArrayList<>();
    }
    
    public void adicionarTransacao(Transacao t) {
        transacoes.add(t);
        fireTableRowsInserted(transacoes.size()-1, transacoes.size()-1);
    }
    
    @Override
    public int getRowCount() { return transacoes.size(); }
    
    @Override
    public int getColumnCount() { return colunas.length; }
    
    @Override
    public String getColumnName(int coluna) { return colunas[coluna]; }
    
    @Override
    public Class<?> getColumnClass(int indiceColuna) {
        if (indiceColuna == 1) return Double.class;
        return String.class;
    }
    
    @Override
    public Object getValueAt(int linha, int coluna) {
        Transacao t = transacoes.get(linha);
        switch (coluna) {
            case 0: return t.getDescricao();
            case 1: return t.getValor();
            case 2: return t.getCategoria();
            case 3: return t.getDataFormatada();
            case 4: return t.getTipo();
            default: return null;
        }
    }
}