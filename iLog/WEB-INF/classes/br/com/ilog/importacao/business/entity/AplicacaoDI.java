package br.com.ilog.importacao.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.cits.commons.citsbusiness.util.Identificavel;

@Entity
@Table( name = "APLICACAO_DI" )
public class AplicacaoDI implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 1799098324092859861L;

	@Id
	private Long id;
	
	@Column( name = "DESCRICAO", length = 25 )
	private String desricao;
	
	@Override
	public Long getPK() {
		return id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the desricao
	 */
	public String getDesricao() {
		return desricao;
	}


	/**
	 * @param desricao the desricao to set
	 */
	public void setDesricao(String desricao) {
		this.desricao = desricao;
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
		if (!(obj instanceof AplicacaoDI))
			return false;
		AplicacaoDI other = (AplicacaoDI) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
}
