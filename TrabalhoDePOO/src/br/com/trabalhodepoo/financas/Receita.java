package br.com.trabalhodepoo.financas;

import java.time.LocalDate;

public class Receita extends Transacao {
	
	Receita(double valor, String descricao, LocalDate data){
		super(valor, descricao, data);
	}

	@Override
	public double calcularImpacto() {
		return super.getValor();
	}

	@Override
	public String getResumo() {
		return "Receita: " + super.getDescricao() + " (-R$ " + super.getValor() + ")";
	}

	@Override
	public String getTIpo() {
		return "Receita";
	}
	
}
