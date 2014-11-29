package br.com.ilog.importacao.business.dto;

import java.math.BigDecimal;

/**
 * @author Heber Santiago
 *
 */
public class SapItemDITaxa {

	private String nrDI;
	
	private String numeroPO;
	
	private String itemPO;
	
	private String tipoImposto;
	
	private BigDecimal baseNormal;
	
	private BigDecimal baseExcluida;
	
	private BigDecimal baseOutras;
	
	private BigDecimal aliquota;
	
	private BigDecimal valorImposto;
	
	private String lancarImposto;
	
	private String contaContabil;

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
	 * @return the tipoImposto
	 */
	public String getTipoImposto() {
		return tipoImposto;
	}

	/**
	 * @param tipoImposto the tipoImposto to set
	 */
	public void setTipoImposto(String tipoImposto) {
		this.tipoImposto = tipoImposto;
	}

	/**
	 * @return the baseNormal
	 */
	public BigDecimal getBaseNormal() {
		return baseNormal;
	}

	/**
	 * @param baseNormal the baseNormal to set
	 */
	public void setBaseNormal(BigDecimal baseNormal) {
		this.baseNormal = baseNormal;
	}

	/**
	 * @return the baseExcluida
	 */
	public BigDecimal getBaseExcluida() {
		return baseExcluida;
	}

	/**
	 * @param baseExcluida the baseExcluida to set
	 */
	public void setBaseExcluida(BigDecimal baseExcluida) {
		this.baseExcluida = baseExcluida;
	}

	/**
	 * @return the baseOutras
	 */
	public BigDecimal getBaseOutras() {
		return baseOutras;
	}

	/**
	 * @param baseOutras the baseOutras to set
	 */
	public void setBaseOutras(BigDecimal baseOutras) {
		this.baseOutras = baseOutras;
	}

	/**
	 * @return the aliquota
	 */
	public BigDecimal getAliquota() {
		return aliquota;
	}

	/**
	 * @param aliquota the aliquota to set
	 */
	public void setAliquota(BigDecimal aliquota) {
		this.aliquota = aliquota;
	}

	/**
	 * @return the valorImposto
	 */
	public BigDecimal getValorImposto() {
		return valorImposto;
	}

	/**
	 * @param valorImposto the valorImposto to set
	 */
	public void setValorImposto(BigDecimal valorImposto) {
		this.valorImposto = valorImposto;
	}

	/**
	 * @return the lancarImposto
	 */
	public String getLancarImposto() {
		return lancarImposto;
	}

	/**
	 * @param lancarImposto the lancarImposto to set
	 */
	public void setLancarImposto(String lancarImposto) {
		this.lancarImposto = lancarImposto;
	}

	/**
	 * @return the contaContabil
	 */
	public String getContaContabil() {
		return contaContabil;
	}

	/**
	 * @param contaContabil the contaContabil to set
	 */
	public void setContaContabil(String contaContabil) {
		this.contaContabil = contaContabil;
	}

	/**
	 * @return the itemPO
	 */
	public String getItemPO() {
		return itemPO;
	}

	/**
	 * @param itemPO the itemPO to set
	 */
	public void setItemPO(String itemPO) {
		this.itemPO = itemPO;
	}
	
}
