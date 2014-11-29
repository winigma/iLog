package br.com.ilog.cadastro.business.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 */
@Entity
@Table( name = "DATA_VIGENCIA" )
public class DataVigencia implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -3172003481661381189L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "data_vigencia_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	@Column( name = "ID" )
	private Long id;
	
	@Column( name = "VIGENCIA_INICIAL" )
	@NotNull(message="notnull")
	@Temporal(TemporalType.DATE)
	private Date vigenciaInical;
	
	@NotNull(message="notnull")
	@Column( name = "VIGENCIA_FINAL" )
	@Temporal(TemporalType.DATE)
	private Date vigenciaFinal;
	
	@NotNull(message="notnull")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn( name = "ID_ITEM_CHECKLIST",insertable=true )
	@Fetch(FetchMode.JOIN)
	@OnDelete(action=OnDeleteAction.CASCADE)
	private ItemChecklist itemCheckList;
	
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

	public Date getVigenciaInical() {
		return vigenciaInical;
	}

	public void setVigenciaInical(Date vigenciaInical) {
		this.vigenciaInical = vigenciaInical;
	}

	public Date getVigenciaFinal() {
		return vigenciaFinal;
	}

	public void setVigenciaFinal(Date vigenciaFinal) {
		this.vigenciaFinal = vigenciaFinal;
	}

	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DataVigencia))
			return false;
		DataVigencia other = (DataVigencia) obj;
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

	public void setItemCheckList(ItemChecklist itemCheckList) {
		this.itemCheckList = itemCheckList;
	}

	public ItemChecklist getItemCheckList() {
		return itemCheckList;
	}
}
