package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

public class BasicFiltroMateriaPrima implements Serializable {

	/** */
	private static final long serialVersionUID = 5681252595719435976L;

	private String codigo;

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

}
