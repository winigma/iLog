package br.com.ilog.importacao.business.entityFilter;

import java.math.BigDecimal;

import br.com.ilog.cadastro.business.entity.Moeda;

/**
 * @author Manoel Neto
 * @date   15/09/2012
 * @coment
 *
 */
public class BasicFiltroMapaCusto {
	private Moeda moeda;
	private BigDecimal txCambio;
	private BigDecimal txCambioUSD;
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
	 * @return the txCambio
	 */
	public BigDecimal getTxCambio() {
		return txCambio;
	}
	/**
	 * @param txCambio the txCambio to set
	 */
	public void setTxCambio(BigDecimal txCambio) {
		this.txCambio = txCambio;
	}
	/**
	 * @return the txCambioUSD
	 */
	public BigDecimal getTxCambioUSD() {
		return txCambioUSD;
	}
	/**
	 * @param txCambioUSD the txCambioUSD to set
	 */
	public void setTxCambioUSD(BigDecimal txCambioUSD) {
		this.txCambioUSD = txCambioUSD;
	}
	
}
