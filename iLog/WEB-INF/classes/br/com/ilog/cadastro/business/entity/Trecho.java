package br.com.ilog.cadastro.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 * 
 * Entidade da tabela TRECHO
 *
 */
@Entity
@Table( name = "TRECHO" )
public class Trecho implements Identificavel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3766734306041873460L;

	@Id
	@Column( name = "ID" )
	@SequenceGenerator( name = "gen", sequenceName = "trecho_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	private Long id;
	
	@ManyToOne( fetch = FetchType.EAGER )
	@Label( "lblRota" )
	@JoinColumn( name = "ID_ROTA", insertable = true, updatable = true )
	@Fetch( FetchMode.JOIN )
	@Cascade( CascadeType.SAVE_UPDATE )
	@NotNull(message="notnull")
	private Rota rota;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PAIS_ORIGEM")
	@Label("lblPaisOrigem")
	@NotNull(message="notnull")
	private Pais paisOrigem;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PAIS_DESTINO")
	@Label("lblPaisDestino")
	@NotNull(message="notnull")
	private Pais paisDestino;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CIDADE_ORIGEM")
	@Label("lblCidadeOrigem")
	@NotNull(message="notnull")
	private Cidade cidadeOrigem;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_CIDADE_DESTINO")
	@Label("lblCidadeDestino")
	@NotNull(message="notnull")
	private Cidade cidadeDestino;
	
	@NotNull(message="notnull")
	@Column(name = "QTDE_DIAS")
	@Label( "lblQtdeDias" )
	private Integer quantidadeDias;
	
	@NotNull(message="notnull")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_MODAL")
	@Label( "lblModal" )
	private Modal tipoTransporte;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TERMINAL_ORIGEM")
	@Label("lblTerminalOrigem")
	@NotNull(message="notnull")
	private Terminal terminalOrigem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TERMINAL_DESTINO")
	@Label("lblTerminalDestino")
	@NotNull(message="notnull")
	private Terminal terminalDestino;
	
	@Override
	public Long getPK() {
		return this.id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
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

	public Integer getQuantidadeDias() {
		return quantidadeDias;
	}

	public void setQuantidadeDias(Integer quantidadeDias) {
		this.quantidadeDias = quantidadeDias;
	}

	public Modal getTipoTransporte() {
		return tipoTransporte;
	}

	public void setTipoTransporte(Modal tipoTransporte) {
		this.tipoTransporte = tipoTransporte;
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

	@Transient
	public String getDestino() {
		String destino = "";
		if ( cidadeDestino != null )
			destino = cidadeDestino.getNome() + ", " + cidadeDestino.getEstado().getSigla();
		if ( paisDestino != null )
			destino += " - " + paisDestino.getSigla();
		
		return destino;
	}
	
	@Transient
	public String getOrigem() {
		String origem = "";
		if ( cidadeOrigem != null )
			origem = cidadeOrigem.getNome() + ", " + cidadeOrigem.getEstado().getSigla();
		if ( paisOrigem != null )
			origem += " - " + paisOrigem.getSigla();
		
		return origem; 
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Trecho))
			return false;
		Trecho other = (Trecho) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
}
