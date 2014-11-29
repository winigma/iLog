package br.com.ilog.cadastro.business.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 *
 */
@Entity
@Table(name = "MOTIVO")
public class Motivo implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 4473179206549424281L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "motivo_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	@Column(name = "ID")
	private Long id;

	@Column(name = "DESCRICAO", nullable = false, length = 100)
	@NotEmpty
	@Label("lblMotivo")
	private String descricao;

	@Label("lblTipoOcorrencia")
	@NotNull(message="notnull")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TIPO_OCORRENCIA_ID", nullable = false)
	private TipoOcorrencia tipoOcorrencia;

	@Column(name = "ATIVO", nullable = false)
	@Label("lblAtivo")
	private Boolean ativo;

	@Label("lblProcessos")
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable( name = "MOTIVO_PROCESSO", 
			joinColumns = @JoinColumn(name = "ID_MOTIVO"), 
			inverseJoinColumns = @JoinColumn(name = "ID_PROCESSO") )
	private List<Processo> processos;

	public Motivo() {
		super();
	}

	public Motivo(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public List<Processo> getProcessos() {
		return processos;
	}

	public void setProcessos(List<Processo> processos) {
		this.processos = processos;
	}

	@Override
	public Long getPK() {
		return this.id;
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
		if (!(obj instanceof Motivo))
			return false;
		Motivo other = (Motivo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
