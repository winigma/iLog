package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.cadastro.business.entity.Motivo;
import br.com.ilog.cadastro.business.entity.Processo;
import br.com.ilog.cadastro.business.entity.TipoOcorrencia;


/**
 * @author Heber Santiago
 *
 */
public class BasicFiltroMotivo implements Serializable { 
	/** */
	private static final long serialVersionUID = 4348661740300618919L;

	private String descricao;

	private Boolean ativo;

	private TipoOcorrencia tipoOcorrencia;

	private Motivo motivo;

	private Processo processo;

	public BasicFiltroMotivo() {

	}

	public BasicFiltroMotivo(TipoOcorrencia tipoOcorrencia) {
		this.tipoOcorrencia = tipoOcorrencia;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoOcorrencia getTipoOcorrencia() {
		return tipoOcorrencia;
	}

	public void setTipoOcorrencia(TipoOcorrencia tipoOcorrencia) {
		this.tipoOcorrencia = tipoOcorrencia;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

}
