package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.cadastro.business.entity.Processo;

/**
 * @author Heber Santiago
 */

public class BasicFiltroTipoOcorrencia implements Serializable {

	/** */
	private static final long serialVersionUID = 4663160892533228129L;
	
	private String descricao;
	private Boolean ativo;
	private Processo processo;
	
	public BasicFiltroTipoOcorrencia() {
		
	}

	public BasicFiltroTipoOcorrencia(boolean b) {
		ativo = b;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}