package br.com.ilog.importacao.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.cadastro.business.entity.Incoterm;

public class BasicFiltroProcBroker implements Serializable {

	/** */
	private static final long serialVersionUID = -4311960402576610992L;

	private String numeroDI;
	
	private Integer ufrDespacho;
	
	private String hawb;
	
	private String numMaster;
	
	private Incoterm incoterm;

	/**
	 * @return the numeroDI
	 */
	public String getNumeroDI() {
		return numeroDI;
	}

	/**
	 * @param numeroDI the numeroDI to set
	 */
	public void setNumeroDI(String numeroDI) {
		this.numeroDI = numeroDI;
	}

	/**
	 * @return the ufrDespacho
	 */
	public Integer getUfrDespacho() {
		return ufrDespacho;
	}

	/**
	 * @param ufrDespacho the ufrDespacho to set
	 */
	public void setUfrDespacho(Integer ufrDespacho) {
		this.ufrDespacho = ufrDespacho;
	}

	/**
	 * @return the hawb
	 */
	public String getHawb() {
		return hawb;
	}

	/**
	 * @param hawb the hawb to set
	 */
	public void setHawb(String hawb) {
		this.hawb = hawb;
	}

	/**
	 * @return the numMaster
	 */
	public String getNumMaster() {
		return numMaster;
	}

	/**
	 * @param numMaster the numMaster to set
	 */
	public void setNumMaster(String numMaster) {
		this.numMaster = numMaster;
	}

	/**
	 * @return the incoterm
	 */
	public Incoterm getIncoterm() {
		return incoterm;
	}

	/**
	 * @param incoterm the incoterm to set
	 */
	public void setIncoterm(Incoterm incoterm) {
		this.incoterm = incoterm;
	}
	
}
