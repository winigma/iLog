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

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.UnidadeMedida;

@Entity
@Table(name = "ITEM_PO" )
public class ItemPO implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -1135655658629444139L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "item_po_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@JoinColumn(name = "ID_PO")
	@ManyToOne(fetch = FetchType.LAZY)
	@Label("lblPO")
	//@Fetch(FetchMode.JOIN)
	//@Cascade( value = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE } )
	private PO po;
	
	@JoinColumn(name = "ID_UNIDADE")
	@ManyToOne( fetch = FetchType.LAZY )
	private UnidadeMedida unidadeMedida;
	
	@JoinColumn(name = "ID_MOEDA")
	@ManyToOne( fetch = FetchType.LAZY )
	private Moeda moeda;
	
	@Column(name = "NR_ITEM")
	private Integer numeroItem;
	
	@Column(name = "PART_NUMBER", length = 20 )
	private String partNumber;
	
	@Column( name = "DESCRICAO_PROD", length = 80 )
	private String descricaoProduto;
	
	@Column(name = "NR_QUANTIDADE" )
	private Integer quantidade;
	
	@Column(name = "QTD_SALDO" )
	private Integer quantidadeSaldo;
	
	@Column(name = "PRECO_UNIT", scale = 4, precision = 12)
	private BigDecimal precoUnitario;

	@Column(name = "VL_TOTAL", scale = 4, precision = 12)
	private BigDecimal valorTotal;
	
	@Column(name = "VL_TOTAL_SALDO", scale = 4, precision = 12)
	private BigDecimal valorTotalSaldo;
	
	@Override
	public Long getPK() {
		// TODO Auto-generated method stub
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
	 * @return the po
	 */
	public PO getPo() {
		return po;
	}

	/**
	 * @param po the po to set
	 */
	public void setPo(PO po) {
		this.po = po;
	}

	/**
	 * @return the unidadeMedida
	 */
	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	/**
	 * @param unidadeMedida the unidadeMedida to set
	 */
	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
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
	 * @return the numeroItem
	 */
	public Integer getNumeroItem() {
		return numeroItem;
	}

	/**
	 * @param numeroItem the numeroItem to set
	 */
	public void setNumeroItem(Integer numeroItem) {
		this.numeroItem = numeroItem;
	}

	/**
	 * @return the partNumber
	 */
	public String getPartNumber() {
		return partNumber;
	}

	/**
	 * @param partNumber the partNumber to set
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	/**
	 * @return the descricaoProduto
	 */
	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	/**
	 * @param descricaoProduto the descricaoProduto to set
	 */
	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	/**
	 * @return the quantidade
	 */
	public Integer getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * @return the quantidadeSaldo
	 */
	public Integer getQuantidadeSaldo() {
		return quantidadeSaldo;
	}

	/**
	 * @param quantidadeSaldo the quantidadeSaldo to set
	 */
	public void setQuantidadeSaldo(Integer quantidadeSaldo) {
		this.quantidadeSaldo = quantidadeSaldo;
	}

	/**
	 * @return the precoUnitario
	 */
	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	/**
	 * @param precoUnitario the precoUnitario to set
	 */
	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ItemPO))
			return false;
		ItemPO other = (ItemPO) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
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

	/**
	 * @return the valorTotal
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the valorTotalSaldo
	 */
	public BigDecimal getValorTotalSaldo() {
		return valorTotalSaldo;
	}

	/**
	 * @param valorTotalSaldo the valorTotalSaldo to set
	 */
	public void setValorTotalSaldo(BigDecimal valorTotalSaldo) {
		this.valorTotalSaldo = valorTotalSaldo;
	}
}
