package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entity.Pais;

/**
 * @author Heber Santiago
 */
public class BasicFiltroTerminal implements Serializable {

	/** */
	private static final long serialVersionUID = 1920156765411497864L;
	
	private String sigla;
	private Pais pais;
	private Estado estado;
	private Cidade cidade;
	private String nomeTerminal;

	public BasicFiltroTerminal() {
		super();
	}
	
	public BasicFiltroTerminal( Pais pais, Estado estado, Cidade cidade ) {
		this.pais = pais;
		this.estado = estado;
		this.cidade = cidade;
	}
	
	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getNomeTerminal() {
		return nomeTerminal;
	}

	public void setNomeTerminal(String nomeTerminal) {
		this.nomeTerminal = nomeTerminal;
	}

}
