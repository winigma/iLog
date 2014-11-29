package br.com.ilog.cadastro.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "INCOTERM")
public class Incoterm implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 1874285353042947754L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "incoterm_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;

	@NotNull(message="notnull")
	@Label("lblDescricao")
	@Column(name = "DESCRICAO", length = 50)
	private String descricao;

	@NotNull(message="notnull")
	@Label("lblSigla")
	@Column(name = "SIGLA", length = 3)
	private String sigla;

	@Column(name = "STATUS")
	@Label("lblAtivo")
	private Boolean ativo;

	public Incoterm() {
		super();
	}

	public Incoterm(Long id){
		this.id =  id;
	}
	
	@Override
	public Long getPK() {
		return id;
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
		if (!(obj instanceof Incoterm))
			return false;
		Incoterm other = (Incoterm) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
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
}
