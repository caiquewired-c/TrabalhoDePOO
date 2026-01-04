package br.com.trabalhodepoo.financas;

import java.util.ArrayList;
import java.util.List;

public class ControleFinanceiro {

    private List<MovimentacaoFinanceira> movimentacoes = new ArrayList<>();

    public void adicionarMovimentacao(MovimentacaoFinanceira m) {
        movimentacoes.add(m);
    }

    public double calcularSaldo() {
        double saldo = 0;
        for (MovimentacaoFinanceira m : movimentacoes) {
            saldo += m.calcularImpacto(); // polimorfismo
        }
        return saldo;
    }

    public List<MovimentacaoFinanceira> getMovimentacoes() {
        return movimentacoes;
    }
}
