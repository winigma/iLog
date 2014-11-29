package br.com.ilog.importacao.business.entity;

import java.math.BigDecimal;

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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;

@Entity
@Table( name = "CUSTO_DI" )
public class MapaCusto implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 5771826761504435168L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "custo_di_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;

	@JoinColumn(name = "ID_CUSTO_IMPORTACAO", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CustoImportacao custoImportacao;
	
	@JoinColumn(name = "ID_CARGA", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@Cascade( value = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE } )
	private Carga carga;
	
	@JoinColumn(name = "ID_FORNECEDOR", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private PessoaJuridica fornecedor;
	
	@JoinColumn(name = "ID_MOEDA", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Moeda moeda;
	
	@Column(name = "TX_CAMBIO", scale = 5, precision = 11)
	private BigDecimal taxaCambio;
	
	@Column(name = "VALOR_CUSTO", scale = 5, precision = 11)
	private BigDecimal valorCusto;
	
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
	 * @return the custoImportacao
	 */
	public CustoImportacao getCustoImportacao() {
		return custoImportacao;
	}

	/**
	 * @param custoImportacao the custoImportacao to set
	 */
	public void setCustoImportacao(CustoImportacao custoImportacao) {
		this.custoImportacao = custoImportacao;
	}

	/**
	 * @return the carga
	 */
	public Carga getCarga() {
		return carga;
	}

	/**
	 * @param carga the carga to set
	 */
	public void setCarga(Carga carga) {
		this.carga = carga;
	}

	/**
	 * @return the fornecedor
	 */
	public PessoaJuridica getFornecedor() {
		return fornecedor;
	}

	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(PessoaJuridica fornecedor) {
		this.fornecedor = fornecedor;
	}

	/**
	 * @return the moeda
	 */
	public Moeda getMoeda() {
		return moeda;
	}

	/**
	 * @param moeda the moeda to set
	 */
	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	/**
	 * @return the taxaCambio
	 */
	public BigDecimal getTaxaCambio() {
		return taxaCambio;
	}

	/**
	 * @param taxaCambio the taxaCambio to set
	 */
	public void setTaxaCambio(BigDecimal taxaCambio) {
		this.taxaCambio = taxaCambio;
	}

	/**
	 * @return the valorCusto
	 */
	public BigDecimal getValorCusto() {
		return valorCusto;
	}

	/**
	 * @param valorCusto the valorCusto to set
	 */
	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}

}
