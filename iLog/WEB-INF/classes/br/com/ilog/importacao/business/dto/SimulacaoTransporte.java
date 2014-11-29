package br.com.ilog.importacao.business.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ilog.cadastro.business.entity.Feriado;
import br.com.ilog.cadastro.business.entity.Trecho;

/**
 * Classe auxiliar para Simulacao de transporte.
 * @author Heber Santiago
 * @date 13/07/2012
 */
public class SimulacaoTransporte implements Serializable {

	/** */
	private static final long serialVersionUID = 1950854581137143040L;

	private Integer qtdTotalDias;
	
	private Date dataChegada;
	
	private Date dataPrevista;
	
	/**
	 * trechos da rota simulada. 
	 */
	private List<Trecho> trechos = new ArrayList<Trecho>( 0 );
	
	/**
	 * feriados da rota simulada.
	 */
	private List<Feriado> feridados = new ArrayList<Feriado>( 0 );

	/**
	 * @return the qtdTotalDias
	 */
	public Integer getQtdTotalDias() {
		return qtdTotalDias;
	}

	/**
	 * @param qtdTotalDias the qtdTotalDias to set
	 */
	public void setQtdTotalDias(Integer qtdTotalDias) {
		this.qtdTotalDias = qtdTotalDias;
	}

	/**
	 * @return the dataChegada
	 */
	public Date getDataChegada() {
		return dataChegada;
	}

	/**
	 * @param dataChegada the dataChegada to set
	 */
	public void setDataChegada(Date dataChegada) {
		this.dataChegada = dataChegada;
	}

	/**
	 * @return the dataPrevistaLom
	 */
	public Date getDataPrevista() {
		return dataPrevista;
	}

	/**
	 * @param dataPrevistaLom the dataPrevistaLom to set
	 */
	public void setDataPrevista(Date dataPrevista) {
		this.dataPrevista = dataPrevista;
	}

	/**
	 * @return the trechos
	 */
	public List<Trecho> getTrechos() {
		return trechos;
	}

	/**
	 * @param trechos the trechos to set
	 */
	public void setTrechos(List<Trecho> trechos) {
		this.trechos = trechos;
	}

	/**
	 * @return the feridados
	 */
	public List<Feriado> getFeridados() {
		return feridados;
	}

	/**
	 * @param feridados the feridados to set
	 */
	public void setFeridados(List<Feriado> feridados) {
		this.feridados = feridados;
	}
}
