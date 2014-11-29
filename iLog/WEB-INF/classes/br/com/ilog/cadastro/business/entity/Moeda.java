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

import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 */

@Entity
@Table(name = "MOEDA")
public class Moeda implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -5490999324639285958L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "moeda_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	@Column(name = "ID")
	private Long id;

	@NotNull(message="notnull")
	@NotEmpty
	@Column(name = "SIGLA")
	private String sigla;

	@Column( name = "ID_SAP" )
	private Integer idSap;
	
	@NotNull(message="notnull")
	@NotEmpty
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "ATIVO")
	private Boolean ativo;
	
	@Column( name = "MOEDA_PADRAO" )
	private Boolean moedaPadrao;
	
	public Moeda() {
		super();
	}
	
	public Moeda( Long id ) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long i) {
		this.id = i;
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
		if (!(obj instanceof Moeda))
			return false;
		Moeda other = (Moeda) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

	/**
	 * @return the moedaPadrao
	 */
	public Boolean getMoedaPadrao() {
		return moedaPadrao;
	}

	/**
	 * @param moedaPadrao the moedaPadrao to set
	 */
	public void setMoedaPadrao(boolean moedaPadrao) {
		this.moedaPadrao = moedaPadrao;
	}

	/**
	 * @return the idSap
	 */
	public Integer getIdSap() {
		return idSap;
	}

	/**
	 * @param idSap the idSap to set
	 */
	public void setIdSap(Integer idSap) {
		this.idSap = idSap;
	}

}
