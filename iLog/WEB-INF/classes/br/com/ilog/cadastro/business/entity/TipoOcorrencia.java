package br.com.ilog.cadastro.business.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 */

@Entity
@Table(name = "TIPO_OCORRENCIA")
public class TipoOcorrencia implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 7610491191922164648L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotNull(message="notnull")
	@Column(name = "DESCRICAO")
	private String descricao;

	@NotNull(message="notnull")
	@Column(name = "ATIVO", nullable = false)
	private boolean ativo;

	@ManyToMany
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "OCORRENCIA_PROCESSO",
			joinColumns = @JoinColumn( name = "OCORRENCIA_ID" ),
			inverseJoinColumns = @JoinColumn( name = "PROCESSO_ID" ) )
	private List<Processo> processos;

	public TipoOcorrencia() {
		super();
	}
	
	public TipoOcorrencia( Long id ) {
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Processo> getProcessos() {
		return processos;
	}

	public void setProcessos(List<Processo> processos) {
		this.processos = processos;
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
		if (!(obj instanceof TipoOcorrencia))
			return false;
		TipoOcorrencia other = (TipoOcorrencia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
