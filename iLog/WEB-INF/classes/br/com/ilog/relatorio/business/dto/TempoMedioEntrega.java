package br.com.ilog.relatorio.business.dto;


/**
 * Classe auxiliar para listar dados do relatório do tempo médio de entrega.
 * @author Manoel Neto
 *
 */
public class TempoMedioEntrega {
	private String origem;
	
	private String destino;
	private Number totalDias;
	private Number qtdCarga;
	private String txLocal;
	private Number idOrigem;
	private Number idDestino;
	
	public TempoMedioEntrega(String origem, String destino, Number totalDias,
			Number qtdCarga, String txLocal, Number idOrigem, Number idDestino) {
		super();
		this.origem = origem;
		this.destino = destino;
		this.totalDias = totalDias;
		this.qtdCarga = qtdCarga;
		this.txLocal = txLocal;
		this.idOrigem = idOrigem;
		this.idDestino = idDestino;
	}
	
	
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public Number getTotalDias() {
		return totalDias;
	}
	public void setTotalDias(Double totalDias) {
		this.totalDias = totalDias;
	}
	public Number getQtdCarga() {
		return qtdCarga;
	}
	public void setQtdCarga(Number qtdCarga) {
		this.qtdCarga = qtdCarga;
	}
	public String getTxLocal() {
		return txLocal;
	}
	public void setTxLocal(String txLocal) {
		this.txLocal = txLocal;
	}
	public Number getIdOrigem() {
		return idOrigem;
	}
	public void setIdOrigem(Number idOrigem) {
		this.idOrigem = idOrigem;
	}
	public Number getIdDestino() {
		return idDestino;
	}
	public void setIdDestino(Number idDestino) {
		this.idDestino = idDestino;
	}
	
}
