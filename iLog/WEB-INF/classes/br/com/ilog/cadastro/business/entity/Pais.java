package br.com.ilog.cadastro.business.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * 
 * @author Heber Santiago
 *
 */
@Entity
@Table(name = "pais")
@SequenceGenerator( name = "pais_id_seq", sequenceName = "pais_id_seq" )
public class Pais implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 8615329059375862033L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pais_id_seq" )
	@Column(name = "id")
	private Long id;

	@NotNull(message="notnull")
	@Label("lblPais")
	@Column(name = "nome", length = 255, unique=true )
	private String nome;

	@NotNull(message="notnull")
	@Label("lblSiglaPais")
	@Column(name = "sigla", length = 3, unique=true )
	private String sigla;

	@Enumerated( EnumType.STRING )
	@Column( name = "regiao", nullable = true )
	private Continente regiao;
	
	@Column( name = "padrao" )
	@Label( "lblPadrao" )
	private boolean padrao;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pais")
	@OrderBy(value = "nome")
	private List<Estado> estados;

	public Pais() {
		super();
	}
	
	public Pais( Long id ) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long i) {
		this.id = i;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Override
	public Long getPK() {
		return this.id;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
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
		if (!(obj instanceof Pais))
			return false;
		Pais other = (Pais) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	public boolean isPadrao() {
		return padrao;
	}

	public void setPadrao(boolean padrao) {
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

//	public Continente getRegiao() {
//		return regiao;
//	}
//
//	public void setRegiao(Continente regiao) {
//		this.regiao = regiao;
//	}
}
