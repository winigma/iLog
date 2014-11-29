package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Moeda;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;
import br.com.ilog.cadastro.business.entity.Rota;

/**
 * @author Heber Santiago
 * 
 * @brief Classe de auxilio para filtro de pesquisa em Frete.
 * @since 14/02/2012
 * 
 */
public class BasicFiltroFrete implements Serializable {

	/** */
	private static final long serialVersionUID = -5762003997284572201L;

	private Cidade cidadeOrigem;

	private Cidade cidadeDestino;

	private Pais paisOrigem;

	private Pais paisDestino;

	private PessoaJuridica agenteCarga;

	private Moeda moeda;

	private Boolean expresso;

	private Boolean ativo;

	private Modal modal;

	public BasicFiltroFrete() {
		super();
	}

	public BasicFiltroFrete(Rota rota) {

		this.agenteCarga = rota.getAgenteCarga();
		this.paisOrigem = rota.getPaisOrigem();
		this.paisDestino = rota.getPaisDestino();
		this.cidadeOrigem = rota.getCidadeOrigem();
		this.cidadeDestino = rota.getCidadeDestino();
		this.modal = rota.getTipoTransporte();
	}

	public BasicFiltroFrete(Rota rota, boolean b) {
		this.agenteCarga = rota.getAgenteCarga();
		this.paisOrigem = rota.getPaisOrigem();
		this.paisDestino = rota.getPaisDestino();
		this.cidadeOrigem = rota.getCidadeOrigem();
		this.cidadeDestino = rota.getCidadeDestino();
		this.modal = rota.getTipoTransporte();
		this.ativo = b;
	}

	public Cidade getCidadeOrigem() {
		return cidadeOrigem;
	}

	public void setCidadeOrigem(Cidade cidadeOrigem) {
		this.cidadeOrigem = cidadeOrigem;
	}

	public Cidade getCidadeDestino() {
		return cidadeDestino;
	}

	public void setCidadeDestino(Cidade cidadeDestino) {
		this.cidadeDestino = cidadeDestino;
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

	public PessoaJuridica getAgenteCarga() {
		return agenteCarga;
	}

	public void setAgenteCarga(PessoaJuridica agenteCarga) {
		this.agenteCarga = agenteCarga;
	}

	public Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public Boolean getExpresso() {
		return expresso;
	}

	public void setExpresso(Boolean expresso) {
		this.expresso = expresso;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Modal getModal() {
		return modal;
	}

	public void setModal(Modal modal) {
		this.modal = modal;
	}

}
