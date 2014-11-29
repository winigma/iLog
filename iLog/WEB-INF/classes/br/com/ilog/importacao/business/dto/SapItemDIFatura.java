package br.com.ilog.importacao.business.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.importacao.business.entity.CustoDI;

public class SapItemDIFatura {

	private String nrDI;
	
	private String numeroPO;
	
	private String itemPO;
	
	private CustoDI custo;
	
	private String codigoFornecedor;
	
	private String referenciaDocumento;
	
	private String transacao;
	
	private Date dtLancamento;
	
	private Date dtDocumento;
	
	private Moeda moeda;
	
	private String textoLancamento;
	
	private Date dtVencimento;
	
	private String condicaoPagamento;
	
	private String metodoPagamento;
	
	private String bloqueioPagamento;
	
	private String textoCabecalho;
	
	private String chaveAlocacao;
	
	private BigDecimal taxaCambial;
	
	private BigDecimal quantidade;
	
	private BigDecimal vlUnitario;
	
	private String semCobertura;
	
	private String despesaComplementar;
	
	private String taxlaw4;
	
	private String taxlaw5;

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

	/**
	 * @return the custo
	 */
	public CustoDI getCusto() {
		return custo;
	}

	/**
	 * @param custo the custo to set
	 */
	public void setCusto(CustoDI custo) {
		this.custo = custo;
	}

	/**
	 * @return the codigoFornecedor
	 */
	public String getCodigoFornecedor() {
		return codigoFornecedor;
	}

	/**
	 * @param codigoFornecedor the codigoFornecedor to set
	 */
	public void setCodigoFornecedor(String codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}

	/**
	 * @return the referenciaDocumento
	 */
	public String getReferenciaDocumento() {
		return referenciaDocumento;
	}

	/**
	 * @param referenciaDocumento the referenciaDocumento to set
	 */
	public void setReferenciaDocumento(String referenciaDocumento) {
		this.referenciaDocumento = referenciaDocumento;
	}

	/**
	 * @return the transacao
	 */
	public String getTransacao() {
		return transacao;
	}

	/**
	 * @param transacao the transacao to set
	 */
	public void setTransacao(String transacao) {
		this.transacao = transacao;
	}

	/**
	 * @return the dtLancamento
	 */
	public Date getDtLancamento() {
		return dtLancamento;
	}

	/**
	 * @param dtLancamento the dtLancamento to set
	 */
	public void setDtLancamento(Date dtLancamento) {
		this.dtLancamento = dtLancamento;
	}

	/**
	 * @return the dtDocumento
	 */
	public Date getDtDocumento() {
		return dtDocumento;
	}

	/**
	 * @param dtDocumento the dtDocumento to set
	 */
	public void setDtDocumento(Date dtDocumento) {
		this.dtDocumento = dtDocumento;
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
	 * @return the textoLancamento
	 */
	public String getTextoLancamento() {
		return textoLancamento;
	}

	/**
	 * @param textoLancamento the textoLancamento to set
	 */
	public void setTextoLancamento(String textoLancamento) {
		this.textoLancamento = textoLancamento;
	}

	/**
	 * @return the dtVencimento
	 */
	public Date getDtVencimento() {
		return dtVencimento;
	}

	/**
	 * @param dtVencimento the dtVencimento to set
	 */
	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	/**
	 * @return the condicaoPagamento
	 */
	public String getCondicaoPagamento() {
		return condicaoPagamento;
	}

	/**
	 * @param condicaoPagamento the condicaoPagamento to set
	 */
	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	/**
	 * @return the metodoPagamento
	 */
	public String getMetodoPagamento() {
		return metodoPagamento;
	}

	/**
	 * @param metodoPagamento the metodoPagamento to set
	 */
	public void setMetodoPagamento(String metodoPagamento) {
		this.metodoPagamento = metodoPagamento;
	}

	/**
	 * @return the bloqueioPagamento
	 */
	public String getBloqueioPagamento() {
		return bloqueioPagamento;
	}

	/**
	 * @param bloqueioPagamento the bloqueioPagamento to set
	 */
	public void setBloqueioPagamento(String bloqueioPagamento) {
		this.bloqueioPagamento = bloqueioPagamento;
	}

	/**
	 * @return the textoCabecalho
	 */
	public String getTextoCabecalho() {
		return textoCabecalho;
	}

	/**
	 * @param textoCabecalho the textoCabecalho to set
	 */
	public void setTextoCabecalho(String textoCabecalho) {
		this.textoCabecalho = textoCabecalho;
	}

	/**
	 * @return the chaveAlocacao
	 */
	public String getChaveAlocacao() {
		return chaveAlocacao;
	}

	/**
	 * @param chaveAlocacao the chaveAlocacao to set
	 */
	public void setChaveAlocacao(String chaveAlocacao) {
		this.chaveAlocacao = chaveAlocacao;
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
	 * @return the semCobertura
	 */
	public String getSemCobertura() {
		return semCobertura;
	}

	/**
	 * @param semCobertura the semCobertura to set
	 */
	public void setSemCobertura(String semCobertura) {
		this.semCobertura = semCobertura;
	}

	/**
	 * @return the despesaComplementar
	 */
	public String getDespesaComplementar() {
		return despesaComplementar;
	}

	/**
	 * @param despesaComplementar the despesaComplementar to set
	 */
	public void setDespesaComplementar(String despesaComplementar) {
		this.despesaComplementar = despesaComplementar;
	}

	/**
	 * @return the taxlaw4
	 */
	public String getTaxlaw4() {
		return taxlaw4;
	}

	/**
	 * @param taxlaw4 the taxlaw4 to set
	 */
	public void setTaxlaw4(String taxlaw4) {
		this.taxlaw4 = taxlaw4;
	}

	/**
	 * @return the taxlaw5
	 */
	public String getTaxlaw5() {
		return taxlaw5;
	}

	/**
	 * @param taxlaw5 the taxlaw5 to set
	 */
	public void setTaxlaw5(String taxlaw5) {
		this.taxlaw5 = taxlaw5;
	}

	/**
	 * @return the taxaCambial
	 */
	public BigDecimal getTaxaCambial() {
		return taxaCambial;
	}

	/**
	 * @param taxaCambial the taxaCambial to set
	 */
	public void setTaxaCambial(BigDecimal taxaCambial) {
		this.taxaCambial = taxaCambial;
	}
}
