package br.com.ilog.cadastro.business.entityFilter;

import java.io.Serializable;

import br.com.ilog.cadastro.business.entity.Cidade;
import br.com.ilog.cadastro.business.entity.Estado;
import br.com.ilog.cadastro.business.entity.Modal;
import br.com.ilog.cadastro.business.entity.Pais;
import br.com.ilog.cadastro.business.entity.Rota;
import br.com.ilog.cadastro.business.entity.Terminal;
import br.com.ilog.cadastro.business.entity.Trecho;

/**
 * @author Heber Santiago
 *
 */
public class BasicFiltroTrecho implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4906164185825890656L;
	private Long id;
	private Rota rota;
	private Pais paisOrigem;
	private Pais paisDestino;
	private Estado estadoDestino;
	private Estado estadoOrigem;
	private Cidade cidadeOrigem;
	private Cidade cidadeDestino;
	private Modal modal;
	private Terminal terminalOrigem;
	private Terminal terminalDestino;
	private Integer quantidadeDias;
	
	private Trecho trecho;
	
	public BasicFiltroTrecho() {
		super();
	}
	
	public BasicFiltroTrecho( Trecho trecho ) {
		if ( trecho != null ) {
			this.trecho = trecho;
			this.id = trecho.getId();
			this.rota = trecho.getRota();
			this.paisOrigem = trecho.getPaisOrigem();
			this.cidadeOrigem = trecho.getCidadeOrigem();
			this.terminalOrigem = trecho.getTerminalOrigem();
			this.paisDestino = trecho.getPaisDestino();
			this.cidadeDestino = trecho.getCidadeDestino();
			this.terminalDestino = trecho.getTerminalDestino();
			this.modal = trecho.getTipoTransporte();
			this.quantidadeDias = trecho.getQuantidadeDias();
		}
	}
	
	public Trecho getTrecho( Trecho trecho, BasicFiltroTrecho filtro ) {
		
		trecho.setId( filtro.getId() );
		trecho.setRota( filtro.getRota() );
		trecho.setPaisOrigem( filtro.getPaisOrigem() );
		trecho.setCidadeOrigem( filtro.getCidadeOrigem() );
		trecho.setTerminalOrigem( filtro.getTerminalOrigem() );
		trecho.setPaisDestino( filtro.getPaisDestino() );
		trecho.setCidadeDestino( filtro.getCidadeDestino() );
		trecho.setTerminalDestino( filtro.getTerminalDestino() );
		trecho.setTipoTransporte( filtro.getModal() );
		trecho.setQuantidadeDias( filtro.getQuantidadeDias() );
		
		return trecho;
	}
	
	public Estado getEstadoOrigem() {
		return estadoOrigem;
	}

	public void setEstadoOrigem(Estado estado) {
		this.estadoOrigem = estado;
	}

	public Estado getEstadoDestino() {
		return estadoDestino;
	}

	public void setEstadoDestino(Estado estadoDestino) {
		this.estadoDestino = estadoDestino;
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

	public Modal getModal() {
		return modal;
	}

	public void setModal(Modal modal) {
		this.modal = modal;
	}

	public Terminal getTerminalOrigem() {
		return terminalOrigem;
	}

	public void setTerminalOrigem(Terminal terminalOrigem) {
		this.terminalOrigem = terminalOrigem;
	}

	public Terminal getTerminalDestino() {
		return terminalDestino;
	}

	public void setTerminalDestino(Terminal terminalDestino) {
		this.terminalDestino = terminalDestino;
	}

	/**
	 * @return the quantidadeDias
	 */
	public Integer getQuantidadeDias() {
		return quantidadeDias;
	}

	/**
	 * @param quantidadeDias the quantidadeDias to set
	 */
	public void setQuantidadeDias(Integer quantidadeDias) {
		this.quantidadeDias = quantidadeDias;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the rota
	 */
	public Rota getRota() {
		return rota;
	}

	/**
	 * @param rota the rota to set
	 */
	public void setRota(Rota rota) {
		this.rota = rota;
	}

	/**
	 * @return the trecho
	 */
	public Trecho getTrecho() {
		return trecho;
	}

	/**
	 * @param trecho the trecho to set
	 */
	public void setTrecho(Trecho trecho) {
		this.trecho = trecho;
	}
}
