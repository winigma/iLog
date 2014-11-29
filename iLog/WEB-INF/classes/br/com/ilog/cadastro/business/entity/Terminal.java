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

import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 */

@Entity
@Table(name = "TERMINAL")
public class Terminal implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = 1975724043701314178L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "terminal_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	@Column(name = "ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CIDADE_ID")
	@Label("lblCidade")
	@NotNull(message="notnull")
	private Cidade cidade;

	@NotNull(message="notnull")
	@NotEmpty
	@Label("lblTerminal")
	@Column(name = "NOME", length = 150)
	private String nome;
	
	@NotNull(message="notnull")
	@NotEmpty
	@Label("lblSigla")
	@Column(name = "SIGLA", length = 3)
	private String sigla;
	
	public Terminal() {
		super();
	}

	public Terminal(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
		if (!(obj instanceof Terminal))
			return false;
		Terminal other = (Terminal) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if ( !id.equals( other.getId() ) )
			return false;
		return true;
	}

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
