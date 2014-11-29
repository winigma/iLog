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
 *
 */
@Entity
@Table( name = "TIPO_PACOTE" )
public class TipoPacote implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -7511377692060334947L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "tipo_pacote_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@NotNull(message="notnull")
	@Label( "lblDescricao" )
	@Column( name = "DESCRICAO", length = 50 )
	private String descricao;
	
	@Column( name = "ID_SAP", length = 4 )
	private String idSap;
	
	@Column( name = "SHPUNT", length = 3 )
	@Label( "lblCategoria" )
	private String shpUnt;
	
	@NotNull(message="notnull")
	@Label( "lblSigla" )
	@Column( name = "SIGLA", length = 5 )
	private String sigla;
	
	@Column(name = "ATIVO")
	@Label("lblAtivo")
	private Boolean ativo;
	
	public TipoPacote() {
		super();
	}
	
	public TipoPacote(Long id){
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
		if (!(obj instanceof TipoPacote))
			return false;
		TipoPacote other = (TipoPacote) obj;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
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

	/**
	 * @return the shpUnt
	 */
	public String getShpUnt() {
		return shpUnt;
	}

	/**
	 * @param shpUnt the shpUnt to set
	 */
	public void setShpUnt(String shpUnt) {
		this.shpUnt = shpUnt;
	}
	
}
