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
 * @author Heber Santiago
 */
@Entity
@Table( name = "MATERIA_PRIMA" )
public class MateriaPrima implements Identificavel<Long> {
	
	/** */
	private static final long serialVersionUID = -6238741852961562279L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "materia_prima_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@NotNull( message = "notnull" )
	@Column( name = "CODIGO", length = 5 )
	@Label( "lblCodigo" )
	private String codigo;
	
	@NotNull( message = "notnull" )
	@Column( name = "DESCRICAO", length = 255 )
	@Label( "lblDescricao" )
	private String descricao;
	
	@Column( name = "ATIVO" )
	@Label( "lblAtivo" )
	private boolean ativo;
	
	@Override
	public Long getPK() {
		return id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MateriaPrima))
			return false;
		MateriaPrima other = (MateriaPrima) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

}
