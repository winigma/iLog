package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

/**
 * 
 * @author Wisley Souza
 * @coment Classe auxiliar de filtragem
 * 
 */

public class BasicFiltroTipoPacote implements Serializable {

	private static final long serialVersionUID = 8555815222132786598L;
	private String descricao;
	private String idSap;
	private String shpUnt;
	private String tipopacote;
	private String sigla;
	private Boolean ativo;
	
	public BasicFiltroTipoPacote(  ) {
		super();
	}
	
	public BasicFiltroTipoPacote( boolean ativo ) {
		this.ativo = ativo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getTipopacote() {
		return tipopacote;
	}
	public void setTipopacote(String tipopacote) {
		this.tipopacote = tipopacote;
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

	/**
	 * @return the shpUnt
	 */
	public String getShpUnt() {
		return shpUnt;
	}

	/**
	 * @param shpUnt the shpUnt to set
	 */
	public void setShpUnt(String shpUnt) {
		this.shpUnt = shpUnt;
	}
	
	

}
