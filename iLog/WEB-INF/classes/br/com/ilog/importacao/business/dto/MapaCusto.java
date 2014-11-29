package br.com.ilog.importacao.business.dto;

import java.util.List;

import br.com.ilog.importacao.business.entity.Carga;

/**
 * @author Manoel Neto
 * @date   14/09/2012
 * @coment
 *
 */
public class MapaCusto {
	
	private Carga carga;
	
	private List<ItemInvoiceMapaCusto> itemInvoice;
	private List<Custos> custosDI;
	
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
	 * @return the itemInvoice
	 */
	public List<ItemInvoiceMapaCusto> getItemInvoice() {
		return itemInvoice;
	}
	/**
	 * @param itemInvoice the itemInvoice to set
	 */
	public void setItemInvoice(List<ItemInvoiceMapaCusto> itemInvoice) {
		this.itemInvoice = itemInvoice;
	}
	/**
	 * @return the custosDI
	 */
	public List<Custos> getCustosDI() {
		return custosDI;
	}
	/**
	 * @param custosDI the custosDI to set
	 */
	public void setCustosDI(List<Custos> custosDI) {
		this.custosDI = custosDI;
	}
	
	

}
