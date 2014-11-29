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

import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.UnidadeMedida;

@Entity
@Table( name = "PROC_ITEM_PO" )
public class ProcItemPo implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -7782554855994887737L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "proc_item_po_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@JoinColumn(name = "ID_PROC_ITEM", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcItem procItem;
	
	@Column( name = "PRODUTO_SUFRAMA", length = 11 )
	private String produtoSuframa;
	
	@Column( name = "DETALHE_SUFRAMA", length = 14 )
	private String detalheSuframa;
	

	@Column( name = "DESCRICAO_SUFRAMA", length = 390 )
	private String descricaoSuframa;
	
	@Column( name = "QUANTIDADE", scale = 4, precision = 16 )
	private BigDecimal quantidade;
	
	@Column( name = "UME", length = 4 )
	private String ume;
	
	@JoinColumn(name = "ID_UNIDADE_MEDIDA", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private UnidadeMedida unidadeMedida;
	
	@Column( name = "PESO_LIQUIDO", scale = 4, precision = 16 )
	private BigDecimal pesoLiquido;
	
	@Column( name = "VL_UNITARIO", scale = 4, precision = 16 )
	private BigDecimal vlUnitario;
	
	@Column( name = "CD_MOEDA_FOB" )
	private Long cdMoedaFOB;
	
	@Column( name = "TX_MOEDA_FOB", scale = 5, precision = 15 )
	private BigDecimal txMoedaFob;
	
	@Column( name = "PART_NUMBER", length = 15 )
	private String partNumber;
	
	@Column( name = "NR_ITEM" )
	private Integer nrItem;
	
	@Column( name = "NOME_FABRICANTE", length = 60 )
	private String nomeFabricante;
	
	@Column( name = "ITEM_PO" )
	private Integer itemPo;
	
	@Column( name = "NR_PO", length = 10 )
	private String nrPo;
	
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
	 * @return the procItemPo
	 */
	public ProcItem getProcItem() {
		return procItem;
	}

	/**
	 * @param procItemPo the procItemPo to set
	 */
	public void setProcItem(ProcItem procItemPo) {
		this.procItem = procItemPo;
	}

	/**
	 * @return the produtoSuframa
	 */
	public String getProdutoSuframa() {
		return produtoSuframa;
	}

	/**
	 * @param produtoSuframa the produtoSuframa to set
	 */
	public void setProdutoSuframa(String produtoSuframa) {
		this.produtoSuframa = produtoSuframa;
	}

	/**
	 * @return the detalheSuframa
	 */
	public String getDetalheSuframa() {
		return detalheSuframa;
	}

	/**
	 * @param detalheSuframa the detalheSuframa to set
	 */
	public void setDetalheSuframa(String detalheSuframa) {
		this.detalheSuframa = detalheSuframa;
	}

	/**
	 * @return the quantidade
	 */
	public BigDecimal getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * @return the ume
	 */
	public String getUme() {
		return ume;
	}

	/**
	 * @param ume the ume to set
	 */
	public void setUme(String ume) {
		this.ume = ume;
	}

	/**
	 * @return the pesoLiquido
	 */
	public BigDecimal getPesoLiquido() {
		return pesoLiquido;
	}

	/**
	 * @param pesoLiquido the pesoLiquido to set
	 */
	public void setPesoLiquido(BigDecimal pesoLiquido) {
		this.pesoLiquido = pesoLiquido;
	}

	/**
	 * @return the vlUnitario
	 */
	public BigDecimal getVlUnitario() {
		return vlUnitario;
	}

	/**
	 * @param vlUnitario the vlUnitario to set
	 */
	public void setVlUnitario(BigDecimal vlUnitario) {
		this.vlUnitario = vlUnitario;
	}

	/**
	 * @return the cdMoedaFOB
	 */
	public Long getCdMoedaFOB() {
		return cdMoedaFOB;
	}

	/**
	 * @param cdMoedaFOB the cdMoedaFOB to set
	 */
	public void setCdMoedaFOB(Long cdMoedaFOB) {
		this.cdMoedaFOB = cdMoedaFOB;
	}

	/**
	 * @return the txMoedaFob
	 */
	public BigDecimal getTxMoedaFob() {
		return txMoedaFob;
	}

	/**
	 * @param txMoedaFob the txMoedaFob to set
	 */
	public void setTxMoedaFob(BigDecimal txMoedaFob) {
		this.txMoedaFob = txMoedaFob;
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
	 * @return the nrItem
	 */
	public Integer getNrItem() {
		return nrItem;
	}

	/**
	 * @param nrItem the nrItem to set
	 */
	public void setNrItem(Integer nrItem) {
		this.nrItem = nrItem;
	}

	/**
	 * @return the nomeFabricante
	 */
	public String getNomeFabricante() {
		return nomeFabricante;
	}

	/**
	 * @param nomeFabricante the nomeFabricante to set
	 */
	public void setNomeFabricante(String nomeFabricante) {
		this.nomeFabricante = nomeFabricante;
	}

	/**
	 * @return the itemPo
	 */
	public Integer getItemPo() {
		return itemPo;
	}

	/**
	 * @param itemPo the itemPo to set
	 */
	public void setItemPo(Integer itemPo) {
		this.itemPo = itemPo;
	}

	/**
	 * @return the nrPo
	 */
	public String getNrPo() {
		return nrPo;
	}

	/**
	 * @param nrPo the nrPo to set
	 */
	public void setNrPo(String nrPo) {
		this.nrPo = nrPo;
	}

	/**
	 * @return the descricaoSuframa
	 */
	public String getDescricaoSuframa() {
		return descricaoSuframa;
	}

	/**
	 * @param descricaoSuframa the descricaoSuframa to set
	 */
	public void setDescricaoSuframa(String descricaoSuframa) {
		this.descricaoSuframa = descricaoSuframa;
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

}
