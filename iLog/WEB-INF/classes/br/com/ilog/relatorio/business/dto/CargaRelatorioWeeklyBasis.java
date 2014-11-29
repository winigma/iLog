package br.com.ilog.relatorio.business.dto;


public class CargaRelatorioWeeklyBasis {

	private int semana;
	private String continente;
	private int quantidade;
	private double percentil; 
	
	public CargaRelatorioWeeklyBasis(){
		super();
	}
	
	public CargaRelatorioWeeklyBasis(int semana, String continente,int quantidade) {
		super();
		this.semana = semana;
		this.continente = continente;
		this.quantidade = quantidade;
	}
/**
 * GETS E SETS
 * /

	/**
	 * @return the semana
	 */
	public int getSemana() {
		return semana;
	}


	


	/**
	 * @param semana the semana to set
	 */
	public void setSemana(int semana) {
		this.semana = semana;
	}


	/**
	 * @return the continente
	 */
	public String getContinente() {
		return continente;
	}


	/**
	 * @param continente the continente to set
	 */
	public void setContinente(String continente) {
		this.continente = continente;
	}


	/**
	 * @return the quantidade
	 */
	public int getQuantidade() {
		return quantidade;
	}


	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}


	/**
	 * @return the percentil
	 */
	public Double getPercentil() {
		return percentil;
	}


	/**
	 * @param percentil the percentil to set
	 */
	public void setPercentil(Double percentil) {
		this.percentil = percentil;
	}
}
