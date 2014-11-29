package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

public class BasicFiltroFilial implements Serializable{

	private static final long serialVersionUID = 1L;
	private String codigo;
	private String idSap;
	private String descricao;
	private Boolean ativo;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the idSap
	 */
	public String getIdSap() {
		return idSap;
	}

	/**
	 * @param idSap the idSap to set
	 */
	public void setIdSap(String idSap) {
		this.idSap = idSap;
	}
	

}
