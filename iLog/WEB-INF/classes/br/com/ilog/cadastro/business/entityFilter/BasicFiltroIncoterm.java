package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

public class BasicFiltroIncoterm implements Serializable {

	private static final long serialVersionUID = 4875278133145718555L;

	private String descricao;
	private String incoterm;
	private String sigla;
	private Boolean ativo;

	public BasicFiltroIncoterm() {
		super();
	}
	
	public BasicFiltroIncoterm( Boolean status ) {
		ativo = status;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getIncoterm() {
		return incoterm;
	}

	public void setIncoterm(String incoterm) {
		this.incoterm = incoterm;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
