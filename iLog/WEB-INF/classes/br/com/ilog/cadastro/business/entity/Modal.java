package br.com.ilog.cadastro.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 */

@Entity
@Table( name = "MODAL" )
public class Modal implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 1383817661968016944L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "modal_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@NotNull(message="notnull")
	@Column( name = "DESCRICAO" )
	private String descricao;
	
	@NotNull(message="notnull")
	@Column( name = "ATIVO" )
	private boolean ativo;
	
	@Column( name = "IMG" )
	private String srcImg;
	
	public Modal() {
	}
	
	public Modal( Long id ) {
		this.id = id;
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
		if (!(obj instanceof Modal))
			return false;
		Modal other = (Modal) obj;
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

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getSrcImg() {
		return srcImg;
	}

	public void setSrcImg(String srcImg) {
		this.srcImg = srcImg;
	}

}
