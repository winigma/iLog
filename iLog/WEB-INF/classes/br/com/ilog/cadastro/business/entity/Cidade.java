package br.com.ilog.cadastro.business.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.importacao.business.entity.FollowUpImportTrecho;

/**
 * @author Heber Santiago
 */
@Entity
@Table(name = "cidade")
public class Cidade implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 7177216598858973008L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "cidade_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	@Column(name = "ID")
	private Long id;

	@NotNull(message="notnull")
	@Label("lblCidade")
	@Column(name = "NOME", length = 255 )
	private String nome;
	
	@NotNull(message="notnull")
	@Label("lblSigla")
	@Column(name = "SIGLA", length = 3 )
	private String sigla;

	@NotNull(message="notnull")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ESTADO")
	@Label("lblEstado")
	private Estado estado;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull(message="notnull")
	@JoinColumn(name = "ID_PAIS")
	@Label("lblPais")
	private Pais pais;
	
	@Column( name = "FILIAL" )
	private Boolean filial;
	
	@Column( name = "DESEMBARACO" )
	private Boolean desembaraco;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cidade")
	private List<FollowUpImportTrecho> trechos;
	
	/* TODO ADD em CIDADE Export Trecho
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cidade")
	private List<FollowUpExportTrecho> trechosExp;
	*/
	public Cidade() {
		super();
	}
	
	public Cidade(Long id) {
		this.id = id;
	}
	
	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@Override
	public Long getPK() {
		return this.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Cidade))
			return false;
		Cidade other = (Cidade) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if ( !getId().equals( other.getId() ) ) {
			return false;
		}
		return true;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Boolean getFilial() {
		return filial;
	}

	public void setFilial(Boolean filial) {
		this.filial = filial;
	}

	public Boolean getDesembaraco() {
		return desembaraco;
	}

	public void setDesembaraco(Boolean desembaraco) {
		this.desembaraco = desembaraco;
	}

	public List<FollowUpImportTrecho> getTrechos() {
		return trechos;
	}

	public void setTrechos(List<FollowUpImportTrecho> trechos) {
		this.trechos = trechos;
	}

	/*
	public List<FollowUpImportTrecho> getTrechos() {
		return trechos;
	}

	public void setTrechos(List<FollowUpImportTrecho> trechos) {
		this.trechos = trechos;
	}

	public List<FollowUpExportTrecho> getTrechosExp() {
		return trechosExp;
	}

	public void setTrechosExp(List<FollowUpExportTrecho> trechosExp) {
		this.trechosExp = trechosExp;
	}*/

}