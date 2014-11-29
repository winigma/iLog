package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import br.com.ilog.cadastro.business.entity.Pais;

public class BasicFiltroFeriado implements Serializable {

	/** */
	private static final long serialVersionUID = 2210071105922134301L;
	
	private String nome;
	private String tipo;
	private Date dataInicio;
	private Date dataFim;
	private Pais pais;
	private List<Pais> paises;
	
	public BasicFiltroFeriado() {
		super();
	}
	
	public BasicFiltroFeriado( Date inicio, Date fim ) {
		dataInicio = inicio;
		dataFim = fim;
	}
	
	public BasicFiltroFeriado( Date inicio, Date fim, Pais pais ) {
		dataInicio = inicio;
		dataFim = fim;
		this.pais = pais;
	}
	
	public BasicFiltroFeriado( Date inicio, Date fim, List<Pais> paises ) {
		dataInicio = inicio;
		dataFim = fim;
		this.paises = paises;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date data) {
		this.dataInicio = data;
	}
	
	public Date getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	public Pais getPais() {
		return pais;
	}
	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Collection<Pais> getPaises() {
		return paises;
	}

	public void setPaises(List<Pais> paises) {
		this.paises = paises;
	}
	

}
