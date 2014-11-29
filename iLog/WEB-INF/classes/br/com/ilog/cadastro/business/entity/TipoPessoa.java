package br.com.ilog.cadastro.business.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 */
@Entity
@Table(name="TIPO_PESSOA")
public class TipoPessoa implements Identificavel<Long> { 
	
	/** */
	private static final long serialVersionUID = -437999447810810862L;
	
	public TipoPessoa(){
		super();
		
	}

	public TipoPessoa(String tipo ){
		this.tipo = tipo;
		
		
	}
	@Id
	@SequenceGenerator( name = "gen", sequenceName = "tipo_pessoa_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@NotNull(message="notnull") @NotEmpty @Label("lblTipo")
	private String tipo;
	
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TipoPessoa))
			return false;
		TipoPessoa other = (TipoPessoa) obj;
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
