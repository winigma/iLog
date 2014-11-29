package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.cadastro.business.entity.Continente;


/**
 * @author Heber Santiago
 */
public class BasicFiltroPais implements Serializable {

	/** */
	private static final long serialVersionUID = -3473987945390117601L;
	
	private String nomePais;
	private String sigla;
	private Boolean padrao;
	private Continente regiao;

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getSigla() {
		return sigla;
	}

	public String getNomePais() {
		return nomePais;
	}

	public void setNomePais(String nomePais) {
		this.nomePais = nomePais;
	}

	public Boolean getPadrao() {
		return padrao;
	}

	public void setPadrao(Boolean padrao) {
		this.padrao = padrao;
	}

	/**
	 * @return the regiao
	 */
	public Continente getRegiao() {
		return regiao;
	}

	/**
	 * @param regiao the regiao to set
	 */
	public void setRegiao(Continente regiao) {
		this.regiao = regiao;
	}

}
