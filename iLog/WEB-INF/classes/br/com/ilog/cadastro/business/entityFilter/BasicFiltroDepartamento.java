package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;


/**
 * 
 * @author Wisley
 * Filtro de departamentos
 *
 */
public class BasicFiltroDepartamento implements Serializable {

	/** */
	private static final long serialVersionUID = -3284169056160231769L;
	
	private String descricao;
	private Boolean ativo;

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
