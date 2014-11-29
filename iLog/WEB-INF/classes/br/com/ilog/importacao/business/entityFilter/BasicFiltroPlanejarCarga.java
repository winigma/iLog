package br.com.ilog.importacao.business.entityFilter;

import java.io.Serializable;
import java.util.Date;

import br.com.ilog.cadastro.business.entity.Filial;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.importacao.business.entity.StatusCarga;
import br.com.ilog.seguranca.business.entity.Usuario;

public class BasicFiltroPlanejarCarga implements Serializable{

	
	private static final long serialVersionUID = -2492389521148542772L;
	
	
	
	private int numSequencia;
	private int anoSequencia;
	private Filial filial;
	private Date periodoInicio;
	private Date periodoFim;

	private String numInvoice;
	private PessoaJuridica agenteCarga;
	private Usuario resposavelUsuario;
	private Boolean ativo;
	private StatusCarga status;
	
	
	
	
	
	public int getNumSequencia() {
		return numSequencia;
	}
	public void setNumSequencia(int numSequencia) {
		this.numSequencia = numSequencia;
	}
	public int getAnoSequencia() {
		return anoSequencia;
	}
	public void setAnoSequencia(int anoSequencia) {
		this.anoSequencia = anoSequencia;
	}
	public Filial getFilial() {
		return filial;
	}
	public void setFilial(Filial filial) {
		this.filial = filial;
	}
	
	public String getNumInvoice() {
		return numInvoice;
	}
	public void setNumInvoice(String numInvoice) {
		this.numInvoice = numInvoice;
	}
	public PessoaJuridica getAgenteCarga() {
		return agenteCarga;
	}
	public void setAgenteCarga(PessoaJuridica agenteCarga) {
		this.agenteCarga = agenteCarga;
	}
	public Usuario getResposavelUsuario() {
		return resposavelUsuario;
	}
	public void setResposavelUsuario(Usuario resposavelUsuario) {
		this.resposavelUsuario = resposavelUsuario;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public Date getPeriodoInicio() {
		return periodoInicio;
	}
	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}
	public Date getPeriodoFim() {
		return periodoFim;
	}
	public void setPeriodoFim(Date periodoFim) {
		this.periodoFim = periodoFim;
	}
	/**
	 * @return the status
	 */
	public StatusCarga getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusCarga status) {
		this.status = status;
	}


}
