package br.com.ilog.cadastro.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Manoel Neto
 * 
 * Entidade da tabela Parametro_continente.
 * 
 */
@Entity
@Table( name = "PARAMETRO_CONTINENTE" )
public class ParametroContinente implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 3567615228939593186L;

	@Id
	@Column( name = "ID" )
	@SequenceGenerator( name = "gen", sequenceName = "parametro_continente_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@Column( name = "CONTINENTE")
	@Label( "lblContinente" )
	@NotNull(message="notnull")
	@Enumerated( EnumType.STRING )
	private Continente continente;
	
	@Column( name = "PRAZO" )
	@NotNull(message="notnull")
	@Label ( "lblPrazo" )
	private Integer prazo;
	
	@Override
	public Long getPK() {
		return this.id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Continente getContinente() {
		return continente;
	}

	public void setContinente(Continente continente) {
		this.continente = continente;
	}

	public Integer getPrazo() {
		return prazo;
	}

	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
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
		if (!(obj instanceof ParametroContinente))
			return false;
		ParametroContinente other = (ParametroContinente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
