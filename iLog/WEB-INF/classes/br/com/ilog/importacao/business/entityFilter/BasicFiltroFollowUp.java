package br.com.ilog.importacao.business.entityFilter;

import java.io.Serializable;
import java.util.Date;

import br.com.ilog.cadastro.business.entity.Continente;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.seguranca.business.entity.Usuario;

/**
 * 
 * @author Manoel Neto
 * @coment Filtro para FollowUP
 * 
 */
public class BasicFiltroFollowUp implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date dtInicio;
	private Date dtFim;
	private Date dtInicioColeta;
	private Date dtFimColeta;
	private int ano;
	
	private Pais paisOrigem;
	private Pais paisDestino;
	private String aps;
	private String processo;
	private PessoaJuridica supplier;
	private PessoaJuridica agCarga;
	private Continente continente;
	private Usuario responsavel;
	
	
	public Date getDtInicio() {
		return dtInicio;
	}
	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
	}
	public Date getDtFim() {
		return dtFim;
	}
	public void setDtFim(Date dtFim) {
		this.dtFim = dtFim;
	}
	public Pais getPaisOrigem() {
		return paisOrigem;
	}
	public void setPaisOrigem(Pais paisOrigem) {
		this.paisOrigem = paisOrigem;
	}
	public Pais getPaisDestino() {
		return paisDestino;
	}
	public void setPaisDestino(Pais paisDestino) {
		this.paisDestino = paisDestino;
	}
	/**
	 * @return the dtInicioColeta
	 */
	public Date getDtInicioColeta() {
		return dtInicioColeta;
	}
	/**
	 * @param dtInicioColeta the dtInicioColeta to set
	 */
	public void setDtInicioColeta(Date dtInicioColeta) {
		this.dtInicioColeta = dtInicioColeta;
	}
	/**
	 * @return the dtFimColeta
	 */
	public Date getDtFimColeta() {
		return dtFimColeta;
	}
	/**
	 * @param dtFimColeta the dtFimColeta to set
	 */
	public void setDtFimColeta(Date dtFimColeta) {
		this.dtFimColeta = dtFimColeta;
	}
	public String getAps() {
		return aps;
	}
	public void setAps(String aps) {
		this.aps = aps;
	}
	public PessoaJuridica getSupplier() {
		return supplier;
	}
	public void setSupplier(PessoaJuridica supplier) {
		this.supplier = supplier;
	}
	public PessoaJuridica getAgCarga() {
		return agCarga;
	}
	public void setAgCarga(PessoaJuridica agCarga) {
		this.agCarga = agCarga;
	}
	public Continente getContinente() {
		return continente;
	}
	public void setContinente(Continente continente) {
		this.continente = continente;
	}
	public Usuario getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}
	/**
	 * @return the ano
	 */
	public int getAno() {
		return ano;
	}
	/**
	 * @param ano the ano to set
	 */
	public void setAno(int ano) {
		this.ano = ano;
	}
	/**
	 * @return the processo
	 */
	public String getProcesso() {
		return processo;
	}
	/**
	 * @param processo the processo to set
	 */
	public void setProcesso(String processo) {
		this.processo = processo;
	}
	
	}
