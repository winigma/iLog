package br.com.ilog.cadastro.business.entity;

import javax.persistence.Column;
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
 * brief Classe de mapeamento da tabela Filial
 */
@Entity
@Table( name = "FILIAL" )
public class Filial implements Identificavel<Long> {

	/**  */
	private static final long serialVersionUID = -631079451802881686L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "filial_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@Column(name = "CODIGO", length = 4)
	@NotNull(message="notnull")
	@NotEmpty
	@Label("lblCodigo")
	private String codigo;
	
	@Column( name = "ID_SAP", length = 4 )
	private String idSap;
	
	@Column(name = "DESCRICAO")
	@NotNull(message="notnull")
	@NotEmpty
	@Label("lblDescricao")
	private String descricao;
	
	@Column(name = "ATIVO")
	@Label("lblAtivo")
	private Boolean ativo;
	
	@Override
	public Long getPK() {
		return this.id;
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
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the ativo
	 */
	public Boolean getAtivo() {
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Filial))
			return false;
		Filial other = (Filial) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the idSap
	 */
	public String getIdSap() {
		return idSap;
	}

	/**
	 * @param idSap the idSap to set
	 */
	public void setIdSap(String idSap) {
		this.idSap = idSap;
	}
}
