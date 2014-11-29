package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.cadastro.business.entity.Pais;

/**
 * @author Heber Santiago
 */
public class BasicFiltroEstado implements Serializable {

	/** */
	private static final long serialVersionUID = -7029001642467979485L;
	
	private Pais pais;
	private String nomeEstado;
	private String nomePais;
	private String sigla;

	public BasicFiltroEstado() {
		super();
	}
	
	public BasicFiltroEstado(Pais pais) {
		this.pais = pais;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public String getNomePais() {
		return nomePais;
	}

	public void setNomePais(String nomePais) {
		this.nomePais = nomePais;
	}

	public String getNomeEstado() {
		if (nomeEstado != null)
			nomeEstado = nomeEstado.toLowerCase();

		return nomeEstado;
	}

	public void setNomeEstado(String nomeEstado) {
		this.nomeEstado = nomeEstado;
	}

	public String getSigla() {
		if (sigla != null)
			sigla = sigla.toLowerCase();

		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
}