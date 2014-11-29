package br.com.ilog.cadastro.business.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 * 
 * Entidade da tabela ROTA.
 *
 */
@Table( name = "ROTA" )
@Entity
public class Rota implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 124522223469321675L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "rota_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	@Column(name = "ID")
	private Long id;

	@Column( name = "CODIGO", length = 5 )
	@Label("lblCodigo")
	@NotNull( message = "notnull" )
	private String codigo;
	
	@NotNull(message="notnull")
	@Column(name = "QTDE_DIAS")
	@Label( "lblSLATransporte" )
	private Integer quantidadeDias;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PAIS_ORIGEM")
	@Label("lblPaisOrigem")
	@NotNull(message="notnull")
	private Pais paisOrigem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PAIS_DESTINO")
	@Label("lblPaisDestino")
	@NotNull(message="notnull")
	private Pais paisDestino;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CIDADE_ORIGEM")
	@Label("lblCidadeOrigem")
	@NotNull(message="notnull")
	private Cidade cidadeOrigem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CIDADE_DESTINO")
	@Label("lblCidadeDestino")
	@NotNull(message="notnull")
	private Cidade cidadeDestino;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_AGENTE_CARGA")
	@Label("lblAgenteCarga")
	@NotNull(message="notnull")
	private PessoaJuridica agenteCarga;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_MODAL")
	@Label( "lblModal" )
	private Modal tipoTransporte;
	
	@Column(name = "CRITICO")
	@Label( "lblCritico" )
	private Boolean critico;
	
	@Column( name = "QUANTIDADE_DIAS_TRECHO" )
	private Integer quantidadeDiasTrecho;
	
	@Column(name = "ATIVO")
	@Label( "lblAtivo" )
	private boolean ativo;
	
	@OneToMany( mappedBy = "rota", fetch = FetchType.LAZY)
	@Cascade( value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN } )
	@OnDelete( action = OnDeleteAction.CASCADE )
	private List<Trecho> trechos;
	
	public Rota() {
		super();
	}
	
	public Rota( Long id ) {
		this.id = id;
	}
	
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
	public Integer getQuantidadeDias() {
		return quantidadeDias;
	}
	public void setQuantidadeDias(Integer quantidadeDias) {
		this.quantidadeDias = quantidadeDias;
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
	public PessoaJuridica getAgenteCarga() {
		return agenteCarga;
	}
	public void setAgenteCarga(PessoaJuridica agenteCarga) {
		this.agenteCarga = agenteCarga;
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
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
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
	public List<Trecho> getTrechos() {
		return trechos;
	}
	public void setTrechos(List<Trecho> trechos) {
		this.trechos = trechos;
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
		if (!(obj instanceof Rota))
			return false;
		Rota other = (Rota) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Integer getQuantidadeDiasTrecho() {
		return quantidadeDiasTrecho;
	}

	public void setQuantidadeDiasTrecho(Integer quantidadeDiasTrecho) {
		this.quantidadeDiasTrecho = quantidadeDiasTrecho;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}
