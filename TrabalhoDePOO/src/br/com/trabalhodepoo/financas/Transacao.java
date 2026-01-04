package br.com.trabalhodepoo.financas;

import java.time.LocalDate;

public abstract class Transacao implements MovimentacaoFinanceira  {
	private double valor;
	private String descricao;
	private LocalDate data;
	
	public Transacao(double valor, String descricao, LocalDate data) {
		this.valor = valor;
		this.descricao = descricao;
		this.data = data;
	}
	
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public LocalDate getData() {
		return data;
	}
	
	public abstract String getTIpo();
}
