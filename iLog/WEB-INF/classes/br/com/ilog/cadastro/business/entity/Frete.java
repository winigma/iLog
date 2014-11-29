package br.com.ilog.cadastro.business.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 *
 */
@Table( name = "FRETE" )
@Entity
public class Frete implements Identificavel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3830391008622094644L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "frete_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@JoinColumn( name = "ID_ROTA" )
	@ManyToOne( fetch = FetchType.LAZY )
	@NotNull( message = "notnull" )
	@Label("lblRota")
	private Rota rota;
	
	@JoinColumn( name = "ID_MOEDA" )
	@ManyToOne( fetch = FetchType.LAZY )
	@NotNull( message = "notnull" )
	@Label("lblMoeda")
	private Moeda moeda;
	
	@Column(name = "ATIVO")
	@Label( "lblAtivo" )
	private boolean ativo;
	
	@Column(name = "VL_MINIMO", scale = 5, precision = 11)
	@Label("lblVlMinimo")
	private BigDecimal vlMinimo;
	
	@Column(name = "TX_COMBUSTIVEL", scale = 5, precision = 11)
	@Label("lblTxCombustivel")
	private BigDecimal txCombustivel;
	
	@Column(name = "TX_SEGURO", scale = 5, precision = 11)
	@Label("lblTxSeguro")
	private BigDecimal txSeguro;
	
	@OneToMany(mappedBy = "frete", fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<TaxaFrete> taxasFrete;
	
	public Frete() {
		super();
	}
	
	public Frete( Long id ) {
		this.id = id;
	}
	
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

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public BigDecimal getVlMinimo() {
		return vlMinimo;
	}

	public void setVlMinimo(BigDecimal vlMinimo) {
		this.vlMinimo = vlMinimo;
	}

	public BigDecimal getTxCombustivel() {
		return txCombustivel;
	}

	public void setTxCombustivel(BigDecimal txCombustivel) {
		this.txCombustivel = txCombustivel;
	}

	public BigDecimal getTxSeguro() {
		return txSeguro;
	}

	public void setTxSeguro(BigDecimal txSeguro) {
		this.txSeguro = txSeguro;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Frete))
			return false;
		Frete other = (Frete) obj;
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

	public List<TaxaFrete> getTaxasFrete() {
		return taxasFrete;
	}

	public void setTaxasFrete(List<TaxaFrete> taxasFrete) {
		this.taxasFrete = taxasFrete;
	}
	
}
