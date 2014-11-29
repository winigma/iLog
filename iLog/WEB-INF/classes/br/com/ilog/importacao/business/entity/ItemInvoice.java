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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.Projeto;

@Entity
@Table( name = "ITEM_INVOICE" )
public class ItemInvoice implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 1728560635227106212L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "item_invoice_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@Label("lblInvoice")
	@JoinColumn(name = "ID_INVOICE", insertable=true, updatable=true)
	@Fetch(FetchMode.JOIN)
	//@Cascade( value = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.SAVE_UPDATE } )
	private Invoice invoice;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@Label("lblProjeto")
	@JoinColumn(name = "ID_PROJETO", nullable = true )
	private Projeto projeto;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_ITEM_PO", nullable = false )
	@NotNull
	private ItemPO itemPO;
	
	
	@NotNull(message="notnull")
	@Label( "lblItem" )
	@Column( name = "ITEM" )
	private Integer item;
	
	@NotNull(message="notnull")
	@Label( "lblDescricao" )
	@Column( name = "DESCRICAO", length = 100 )
	private String descricao;
	
		
	@NotNull(message="notnull")
	@Label( "lblQuantidade" )
	@Column( name = "QUANTIDADE" )
	private Integer quantidade;
	
	@Label( "lblUnidadeMedida" )
	@Column( name = "UNIDADE_MEDIDA" )
	private String unidadeMedida;
	
	@Label( "lblVendorPartNum" )
	@Column( name = "VENDOR_PART_NUM" , length = 20)
	private String vendorPartNum;
	
	@NotNull(message="notnull")
	@Label( "lblPrecoUnit" )
	@Column( name = "PRECO_UNIT", scale = 5, precision = 12 )
	private BigDecimal precoUnit;

	@Label( "lblComplemento" )
	@Column( name = "COMPLEMENTO", length = 150)
	private String complemento;
	
	@NotNull(message="notnull")
	@Label( "lblTotalItem" )
	@Column( name = "TOTAL", scale = 5, precision = 15 )
	private BigDecimal total;
	
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public String getVendorPartNum() {
		return vendorPartNum;
	}

	public void setVendorPartNum(String vendorPartNum) {
		this.vendorPartNum = vendorPartNum;
	}

	public BigDecimal getPrecoUnit() {
		return precoUnit;
	}

	public void setPrecoUnit(BigDecimal precoUnit) {
		this.precoUnit = precoUnit;
	}

	public BigDecimal getTotal() {
		total = BigDecimal.ZERO;
		if ( precoUnit != null && quantidade != null ) {
			total = precoUnit.multiply( BigDecimal.valueOf( quantidade ) );
		}
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	/**
	 * @return the complemento
	 */
	public String getComplemento() {
		return complemento;
	}

	/**
	 * @param complemento the complemento to set
	 */
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	/**
	 * @return the itemPO
	 */
	public ItemPO getItemPO() {
		return itemPO;
	}

	/**
	 * @param itemPO the itemPO to set
	 */
	public void setItemPO(ItemPO itemPO) {
		this.itemPO = itemPO;
	}

}
