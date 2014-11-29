package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entity.Pais;

/**
 * @author Heber Santiago
 */
public class BasicFiltroCidade implements Serializable {

	/** */
	private static final long serialVersionUID = 7037816069614792918L;
	
	private Pais pais;
	private Estado estado;
	private String nomeCidade;
	private String siglaCidade;
	
	public BasicFiltroCidade() {
	}
	
	public BasicFiltroCidade(Estado estado) {
		this.estado = estado;
	}
	
	public BasicFiltroCidade(Pais pais, Estado estado) {
		this.pais = pais;
		this.estado = estado;
	}
	
	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	public String getSiglaCidade() {
		return siglaCidade;
	}

	public void setSiglaCidade(String siglaCidade) {
		this.siglaCidade = siglaCidade;
	}

}
