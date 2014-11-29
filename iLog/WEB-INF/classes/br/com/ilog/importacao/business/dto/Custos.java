package br.com.ilog.importacao.business.dto;

import java.math.BigDecimal;

import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.importacao.business.entity.CustoDI;

/**
 * @author Manoel Neto
 * @date   04/10/2012
 * @coment Classe Auxiliar para Montar o Mapa de Custo.
 *
 */
public class Custos {
	private BigDecimal valorUSD;
	private BigDecimal valorReal;
	private CustoDI custoDI;
	private Moeda moeda;
	
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
	 * @return the valorReal
	 */
	public BigDecimal getValorReal() {
		return valorReal;
	}
	/**
	 * @param valorReal the valorReal to set
	 */
	public void setValorReal(BigDecimal valorReal) {
		this.valorReal = valorReal;
	}
	/**
	 * @return the custoDI
	 */
	public CustoDI getCustoDI() {
		return custoDI;
	}
	/**
	 * @param custoDI the custoDI to set
	 */
	public void setCustoDI(CustoDI custoDI) {
		this.custoDI = custoDI;
	}
}
