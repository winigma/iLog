package br.com.ilog.importacao.business.dto;

import java.math.BigDecimal;

/**
 * Classe auxiliar para Export SAP
 * @author Heber Santiago
 *
 */
public class SapItemDI {

	private String nrDI;
	
	private String numeroPO;
	
	private int itemPO;
	
	private String adicaoDI;
	
	private String itemAdicaoDI;
	
	private BigDecimal quantidade;
	
	private BigDecimal precoUnitario;
	
	private BigDecimal freteUnitario;
	
	private BigDecimal outrasDespesas;
	
	private String cfop;
	
	private String ncm;
	
	private String textoIcms;
	
	private String textoIpi;
	
	private String origemMaterial;
	
	private String usoMaterial;
	
	private String taxLW4;
	
	private String taxLW5;
	
	private String sequencialDI;
	
	private String fabricante;
	
	private BigDecimal percentual;
	
	/**
	 * @return the numeroPO
	 */
	public String getNumeroPO() {
		return numeroPO;
	}
	/**
	 * @param numeroPO the numeroPO to set
	 */
	public void setNumeroPO(String numeroPO) {
		this.numeroPO = numeroPO;
	}
	/**
	 * @return the itemPO
	 */
	public Integer getItemPO() {
		return itemPO;
	}
	/**
	 * @param itemPO the itemPO to set
	 */
	public void setItemPO(int itemPO) {
		this.itemPO = itemPO;
	}
	/**
	 * @return the adicaoDI
	 */
	public String getAdicaoDI() {
		return adicaoDI;
	}
	/**
	 * @param adicaoDI the adicaoDI to set
	 */
	public void setAdicaoDI(String adicaoDI) {
		this.adicaoDI = adicaoDI;
	}
	/**
	 * @return the itemAdicaoDI
	 */
	public String getItemAdicaoDI() {
		return itemAdicaoDI;
	}
	/**
	 * @param itemAdicaoDI the itemAdicaoDI to set
	 */
	public void setItemAdicaoDI(String itemAdicaoDI) {
		this.itemAdicaoDI = itemAdicaoDI;
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
	 * @return the percoUnitario
	 */
	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}
	/**
	 * @param percoUnitario the percoUnitario to set
	 */
	public void setPrecoUnitario(BigDecimal percoUnitario) {
		this.precoUnitario = percoUnitario;
	}
	/**
	 * @return the freteUnitario
	 */
	public BigDecimal getFreteUnitario() {
		return freteUnitario;
	}
	/**
	 * @param freteUnitario the freteUnitario to set
	 */
	public void setFreteUnitario(BigDecimal freteUnitario) {
		this.freteUnitario = freteUnitario;
	}
	/**
	 * @return the outrasDespesas
	 */
	public BigDecimal getOutrasDespesas() {
		return outrasDespesas;
	}
	/**
	 * @param outrasDespesas the outrasDespesas to set
	 */
	public void setOutrasDespesas(BigDecimal outrasDespesas) {
		this.outrasDespesas = outrasDespesas;
	}
	/**
	 * @return the cfop
	 */
	public String getCfop() {
		return cfop;
	}
	/**
	 * @param cfop the cfop to set
	 */
	public void setCfop(String cfop) {
		this.cfop = cfop;
	}
	/**
	 * @return the ncm
	 */
	public String getNcm() {
		return ncm;
	}
	/**
	 * @param ncm the ncm to set
	 */
	public void setNcm(String ncm) {
		this.ncm = ncm;
	}
	/**
	 * @return the textoIcms
	 */
	public String getTextoIcms() {
		return textoIcms;
	}
	/**
	 * @param textoIcms the textoIcms to set
	 */
	public void setTextoIcms(String textoIcms) {
		this.textoIcms = textoIcms;
	}
	/**
	 * @return the textoIpi
	 */
	public String getTextoIpi() {
		return textoIpi;
	}
	/**
	 * @param textoIpi the textoIpi to set
	 */
	public void setTextoIpi(String textoIpi) {
		this.textoIpi = textoIpi;
	}
	/**
	 * @return the origemMaterial
	 */
	public String getOrigemMaterial() {
		return origemMaterial;
	}
	/**
	 * @param origemMaterial the origemMaterial to set
	 */
	public void setOrigemMaterial(String origemMaterial) {
		this.origemMaterial = origemMaterial;
	}
	/**
	 * @return the usoMaterial
	 */
	public String getUsoMaterial() {
		return usoMaterial;
	}
	/**
	 * @param usoMaterial the usoMaterial to set
	 */
	public void setUsoMaterial(String usoMaterial) {
		this.usoMaterial = usoMaterial;
	}
	/**
	 * @return the taxLW4
	 */
	public String getTaxLW4() {
		return taxLW4;
	}
	/**
	 * @param taxLW4 the taxLW4 to set
	 */
	public void setTaxLW4(String taxLW4) {
		this.taxLW4 = taxLW4;
	}
	/**
	 * @return the taxLW5
	 */
	public String getTaxLW5() {
		return taxLW5;
	}
	/**
	 * @param taxLW5 the taxLW5 to set
	 */
	public void setTaxLW5(String taxLW5) {
		this.taxLW5 = taxLW5;
	}
	/**
	 * @return the sequencialDI
	 */
	public String getSequencialDI() {
		return sequencialDI;
	}
	/**
	 * @param sequencialDI the sequencialDI to set
	 */
	public void setSequencialDI(String sequencialDI) {
		this.sequencialDI = sequencialDI;
	}
	/**
	 * @return the fabricante
	 */
	public String getFabricante() {
		return fabricante;
	}
	/**
	 * @param fabricante the fabricante to set
	 */
	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}
	/**
	 * @return the percentual
	 */
	public BigDecimal getPercentual() {
		return percentual;
	}
	/**
	 * @param percentual the percentual to set
	 */
	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	public BigDecimal getTotal() {
		if ( quantidade != null && precoUnitario != null ) {
			return quantidade.multiply( precoUnitario );
		}
		return BigDecimal.ZERO;
	}
	/**
	 * @return the nrDI
	 */
	public String getNrDI() {
		return nrDI;
	}
	/**
	 * @param nrDI the nrDI to set
	 */
	public void setNrDI(String nrDI) {
		this.nrDI = nrDI;
	}
	
}
