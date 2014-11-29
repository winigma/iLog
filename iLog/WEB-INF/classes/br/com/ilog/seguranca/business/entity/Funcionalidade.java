package br.com.ilog.seguranca.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.cits.commons.citsbusiness.util.Identificavel;

@Entity
@Table( name = "FUNCIONALIDADE", uniqueConstraints = @UniqueConstraint( columnNames = {"DESCRICAO"} ) )
@SequenceGenerator( allocationSize = 1, name = "GEN", sequenceName = "funcionalidade_id_seq" )
public class Funcionalidade implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 151261933548719046L;
	
	@Id
	@GeneratedValue( generator = "GEN", strategy = GenerationType.SEQUENCE )
	private Long id;
	
	@Column( name = "DESCRICAO", length = 100, nullable = false )
	@Enumerated( EnumType.STRING )
	private Role descricao;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Role getDescricao() {
		return descricao;
	}
	public void setDescricao(Role descricao) {
		this.descricao = descricao;
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
		if (!(obj instanceof Funcionalidade))
			return false;
		Funcionalidade other = (Funcionalidade) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if ( !getId().equals( other.getId() ) )
			return false;
		return true;
	}
}
