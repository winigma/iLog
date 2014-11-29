package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;


/**
 * @author Heber Santiago
 */
public class BasicFiltroModal implements Serializable {

	/** */
	private static final long serialVersionUID = -2277876887770000595L;
	
	private String descricao;
	private Boolean ativo;
	
	public BasicFiltroModal() {
		super();
	}
	
	public BasicFiltroModal( Boolean ativo ) {
		this.ativo = ativo;
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
