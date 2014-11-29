package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

/**
 * @author Heber Santiago
 *
 */
public class BasicFiltroProjeto implements Serializable {
	/** */
	private static final long serialVersionUID = 8464333494775688898L;
	
	private String nome;
	private Boolean ativo;

	public BasicFiltroProjeto() {
		super();
	}
	
	public BasicFiltroProjeto( Boolean status ) {
		ativo = status;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
