package br.com.ilog.importacao.business.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.TipoPacote;

public class SapDI {

	private String nrDI;
	
	private String companyCode;
	
	private String branchFilial;
	
	private Date dtRegistroDI;
	
	private Date dtDesembaracoDI;
	
	private String incoterms;
	
	private String awb;
	
	private BigDecimal pesoLiquido;
	
	private BigDecimal pesoBruto;
	
	private String unidadeMedida;
	
	private TipoPacote volume;
	
	private BigDecimal qtdPacotes;
	
	private String tipoNotaFiscal;
	
	private PessoaJuridica agenteCargas;
	
	private String ufDespachante;
	
	private String localDespachante;
	
	private List<SapItemDI> itensDis;
	
	private List<SapItemDITaxa> itensTaxa;
	
	private List<SapItemDIFatura> itensFatura;

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
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}

	/**
	 * @param companyCode the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	/**
	 * @return the branchFilial
	 */
	public String getBranchFilial() {
		return branchFilial;
	}

	/**
	 * @param branchFilial the branchFilial to set
	 */
	public void setBranchFilial(String branchFilial) {
		this.branchFilial = branchFilial;
	}

	/**
	 * @return the dtRegsitroDI
	 */
	public Date getDtRegistroDI() {
		return dtRegistroDI;
	}

	/**
	 * @param dtRegsitroDI the dtRegsitroDI to set
	 */
	public void setDtRegistroDI(Date dtRegsitroDI) {
		dtRegistroDI = dtRegsitroDI;
	}

	/**
	 * @return the dtDesembaraçoDI
	 */
	public Date getDtDesembaracoDI() {
		return dtDesembaracoDI;
	}

	/**
	 * @param dtDesembaraçoDI the dtDesembaraçoDI to set
	 */
	public void setDtDesembaracoDI(Date dtDesembaraçoDI) {
		this.dtDesembaracoDI = dtDesembaraçoDI;
	}

	/**
	 * @return the incoterms
	 */
	public String getIncoterms() {
		return incoterms;
	}

	/**
	 * @param incoterms the incoterms to set
	 */
	public void setIncoterms(String incoterms) {
		this.incoterms = incoterms;
	}

	/**
	 * @return the awb
	 */
	public String getAwb() {
		return awb;
	}

	/**
	 * @param awb the awb to set
	 */
	public void setAwb(String awb) {
		this.awb = awb;
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
	 * @return the pesoBruto
	 */
	public BigDecimal getPesoBruto() {
		return pesoBruto;
	}

	/**
	 * @param pesoBruto the pesoBruto to set
	 */
	public void setPesoBruto(BigDecimal pesoBruto) {
		this.pesoBruto = pesoBruto;
	}

	/**
	 * @return the unidadeMedida
	 */
	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	/**
	 * @param unidadeMedida the unidadeMedida to set
	 */
	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	/**
	 * @return the itensTaxa
	 */
	public List<SapItemDITaxa> getItensTaxa() {
		return itensTaxa;
	}

	/**
	 * @param itensTaxa the itensTaxa to set
	 */
	public void setItensTaxa(List<SapItemDITaxa> itensTaxa) {
		this.itensTaxa = itensTaxa;
	}

	/**
	 * @return the itensDis
	 */
	public List<SapItemDI> getItensDis() {
		return itensDis;
	}

	/**
	 * @param itensDis the itensDis to set
	 */
	public void setItensDis(List<SapItemDI> itensDis) {
		this.itensDis = itensDis;
	}

	/**
	 * @return the volume
	 */
	public TipoPacote getVolume() {
		return volume;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(TipoPacote volume) {
		this.volume = volume;
	}

	/**
	 * @return the qtdPacotes
	 */
	public BigDecimal getQtdPacotes() {
		return qtdPacotes;
	}

	/**
	 * @param qtdPacotes the qtdPacotes to set
	 */
	public void setQtdPacotes(BigDecimal qtdPacotes) {
		this.qtdPacotes = qtdPacotes;
	}

	/**
	 * @return the tipoNotaFiscal
	 */
	public String getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	/**
	 * @param tipoNotaFiscal the tipoNotaFiscal to set
	 */
	public void setTipoNotaFiscal(String tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}

	/**
	 * @return the agenteCargas
	 */
	public PessoaJuridica getAgenteCargas() {
		return agenteCargas;
	}

	/**
	 * @param agenteCargas the agenteCargas to set
	 */
	public void setAgenteCargas(PessoaJuridica agenteCargas) {
		this.agenteCargas = agenteCargas;
	}

	/**
	 * @return the ufDespachante
	 */
	public String getUfDespachante() {
		return ufDespachante;
	}

	/**
	 * @param ufDespachante the ufDespachante to set
	 */
	public void setUfDespachante(String ufDespachante) {
		this.ufDespachante = ufDespachante;
	}

	/**
	 * @return the localDespachante
	 */
	public String getLocalDespachante() {
		return localDespachante;
	}

	/**
	 * @param localDespachante the localDespachante to set
	 */
	public void setLocalDespachante(String localDespachante) {
		this.localDespachante = localDespachante;
	}

	/**
	 * @return the itensFatura
	 */
	public List<SapItemDIFatura> getItensFatura() {
		return itensFatura;
	}

	/**
	 * @param itensFatura the itensFatura to set
	 */
	public void setItensFatura(List<SapItemDIFatura> itensFatura) {
		this.itensFatura = itensFatura;
	}
	
}
