package br.com.ilog.exportacao.business.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;






@Entity
@Table(name = "FOLLOWUP_EXPOTACAO")
public class Followup implements Identificavel<Long> {
	
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "followup_expo_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblCarga")
	@Fetch(FetchMode.JOIN)
	@Cascade( value = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE } )	
	@JoinColumn( name = "ID_CARGA" ,insertable=true, updatable=true)	
	private CargaExp carga;
	
	
	
	

	public CargaExp getCarga() {
		return carga;
	}

	public void setCarga(CargaExp carga) {
		this.carga = carga;
	}

	@Override
	public Long getPK() {
		// TODO Auto-generated method stub
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Followup))
			return false;
		Followup other = (Followup) obj;
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
