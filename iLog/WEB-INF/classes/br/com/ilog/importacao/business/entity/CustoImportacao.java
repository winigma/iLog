package br.com.ilog.importacao.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.cits.commons.citsbusiness.util.Identificavel;

@Entity
@Table( name = "CUSTO_IMPORTACAO" )
public class CustoImportacao implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 915879720915771804L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "custo_importacao_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@Column( name = "DESCRICAO", length = 100 )
	private String descricao;
	
	@Column( name = "DESPESA" )
	private boolean despesa;
	
	@Column( name = "GRUPO_CUSTO", length = 2 )
	private String grupoCusto;
	
	@Column( name = "TIPO_FATURA", length = 1 )
	private String tipoFatura;
	
	@Column( name = "CODIGO_FATURA", length = 10 )
	private String codigoFatura;
	
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
	 * @return the despesa
	 */
	public boolean isDespesa() {
		return despesa;
	}

	/**
	 * @param despesa the despesa to set
	 */
	public void setDespesa(boolean despesa) {
		this.despesa = despesa;
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
		if (!(obj instanceof CustoImportacao))
			return false;
		CustoImportacao other = (CustoImportacao) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	/**
	 * @return the grupoCusto
	 */
	public String getGrupoCusto() {
		return grupoCusto;
	}

	/**
	 * @param grupoCusto the grupoCusto to set
	 */
	public void setGrupoCusto(String grupoCusto) {
		this.grupoCusto = grupoCusto;
	}

	/**
	 * @return the tipoFatura
	 */
	public String getTipoFatura() {
		return tipoFatura;
	}

	/**
	 * @param tipoFatura the tipoFatura to set
	 */
	public void setTipoFatura(String tipoFatura) {
		this.tipoFatura = tipoFatura;
	}

	/**
	 * @return the codigoFatura
	 */
	public String getCodigoFatura() {
		return codigoFatura;
	}

	/**
	 * @param codigoFatura the codigoFatura to set
	 */
	public void setCodigoFatura(String codigoFatura) {
		this.codigoFatura = codigoFatura;
	}
	
}
