package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;

/**
 * @author Heber Santiago
 *
 */
public class BasicFiltroRota implements Serializable {

	/** */
	private static final long serialVersionUID = -3968995230578433131L;

	private Pais paisOrigem;
	
	private Pais paisDestino;
	
	private Cidade cidadeOrigem;
	
	private Cidade cidadeDestino;
	
	private PessoaJuridica agenteCargas;
	
	private Modal tipoTransporte;
	
	private Boolean critico;
	
	private Boolean ativo;

	private String codigo;
	
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

	public PessoaJuridica getAgenteCargas() {
		return agenteCargas;
	}

	public void setAgenteCargas(PessoaJuridica agenteCargas) {
		this.agenteCargas = agenteCargas;
	}

	public Modal getTipoTransporte() {
		return tipoTransporte;
	}

	public void setTipoTransporte(Modal tipoTransporte) {
		this.tipoTransporte = tipoTransporte;
	}

	public Boolean getCritico() {
		return critico;
	}

	public void setCritico(Boolean critico) {
		this.critico = critico;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
}
