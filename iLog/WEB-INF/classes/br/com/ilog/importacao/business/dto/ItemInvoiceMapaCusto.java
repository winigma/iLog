package br.com.ilog.importacao.business.dto;

import java.math.BigDecimal;

import br.com.ilog.importacao.business.entity.ItemInvoice;

/**
 * @author Manoel Neto
 * @date   05/10/2012
 * @coment
 *
 */
public class ItemInvoiceMapaCusto {
	private ItemInvoice itemInvoice;
	private BigDecimal valorUSD;
	private BigDecimal despesaUSD;
	private Integer percentil;
	
	

	/**
	 * @return the itemInvoice
	 */
	public ItemInvoice getItemInvoice() {
		return itemInvoice;
	}
	/**
	 * @param itemInvoice the itemInvoice to set
	 */
	public void setItemInvoice(ItemInvoice itemInvoice) {
		this.itemInvoice = itemInvoice;
	}
	/**
	 * @return the valorUSD
	 */
	public BigDecimal getValorUSD() {
		return valorUSD;
	}
	/**
	 * @param valorUSD the valorUSD to set
	 */
	public void setValorUSD(BigDecimal valorUSD) {
		this.valorUSD = valorUSD;
	}
	/**
	 * @return the despesaUSD
	 */
	public BigDecimal getDespesaUSD() {
		return despesaUSD;
	}
	/**
	 * @param despesaUSD the despesaUSD to set
	 */
	public void setDespesaUSD(BigDecimal despesaUSD) {
		this.despesaUSD = despesaUSD;
	}
	/**
	 * @return the percentil
	 */
	public Integer getPercentil() {
		return percentil;
	}
	/**
	 * @param percentil the percentil to set
	 */
	public void setPercentil(Integer percentil) {
		this.percentil = percentil;
	}
	/**
	 * @return the totaValueUSD
	 */
		
	
}
