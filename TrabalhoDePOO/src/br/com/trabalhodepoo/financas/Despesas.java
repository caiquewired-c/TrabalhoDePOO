package br.com.trabalhodepoo.financas;

import java.time.LocalDate;

public class Despesas extends Transacao {
	
	private String categoria;
    public static final String CATEGORIA_ALIMENTACAO = "Alimentação";
    public static final String CATEGORIA_TRANSPORTE = "Transporte";
    public static final String CATEGORIA_LAZER = "Lazer";
    public static final String CATEGORIA_OUTROS = "Outros";

    public Despesas(String descricao, double valor, LocalDate data, String categoria) {
    	super(valor, descricao, data);
    	this.categoria = categoria;
    }
	
	@Override
	public double calcularImpacto() {
		double valor = super.getValor();
		return -valor;
	}

	@Override
	public String getResumo() {
		return "Despesa (" + this.categoria + "): " + super.getDescricao() + " (-R$ " + super.getValor() + ")";
	}

	@Override
	public String getTIpo() {
		return "Despesa";
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
}
