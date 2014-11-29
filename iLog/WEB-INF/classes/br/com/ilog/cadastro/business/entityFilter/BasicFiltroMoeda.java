package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;


/**
 * @author Heber Santiago
 */
public class BasicFiltroMoeda implements Serializable {

	/** */
	private static final long serialVersionUID = -6285221509315806512L;
	
	private String sigla;
	private Integer idSap;
	private String descricao;
	private Boolean ativo;

	public BasicFiltroMoeda() {
		super();
	}
	
	public BasicFiltroMoeda( boolean ativo ) {
		this.ativo = ativo;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getSigla() {
		return sigla;
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
	public Integer getIdSap() {
		return idSap;
	}

	/**
	 * @param idSap the idSap to set
	 */
	public void setIdSap(Integer idSap) {
		this.idSap = idSap;
	}

}
