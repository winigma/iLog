package br.com.ilog.cadastro.business.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

@Entity
@Table( name = "TAXA_FRETE" )
public class TaxaFrete implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -7303864215766834389L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "taxa_frete_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@Label("lblFrete")
	@JoinColumn(name = "ID_FRETE", insertable=true, updatable=true)
	@Fetch(FetchMode.JOIN)
	@Cascade( value = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE } )
	private Frete frete;
	
	@Column( name = "CRITERIO", nullable = false )
	@Label("lblCriterio")
	@NotNull( message = "notnull" )
	@Enumerated( EnumType.STRING )
	private Criterio criterio;
	
	@Column( name = "PESO", nullable = false, scale = 5, precision = 11 )
	@Label("lblPeso")
	@NotNull( message = "notnull" )
	private BigDecimal peso;
	
	@Column( name = "VALOR", nullable = false, scale = 5, precision = 11 )
	@Label("lblValor")
	@NotNull( message = "notnull" )
	private BigDecimal valor;
	
	@Override
	public Long getPK() {
		return id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Frete getFrete() {
		return frete;
	}

	public void setFrete(Frete frete) {
		this.frete = frete;
	}

	public Criterio getCriterio() {
		return criterio;
	}

	public void setCriterio(Criterio criterio) {
		this.criterio = criterio;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TaxaFrete))
			return false;
		TaxaFrete other = (TaxaFrete) obj;
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
