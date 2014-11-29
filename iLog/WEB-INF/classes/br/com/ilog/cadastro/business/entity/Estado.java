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

import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * @author Heber Santiago
 */
@Entity
@Table(name = "ESTADO")
@SequenceGenerator( allocationSize = 1, name = "estado_id_seq", sequenceName = "estado_id_seq" )
public class Estado implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -7347084641330647061L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_id_seq" )
	@Column(name = "id")
	private Long id;

	@NotNull(message="notnull")
	@Label("lblEstado")
	@Column(name = "NOME", length = 255 )
	private String nome;

	@NotNull(message="notnull")
	@Label("lblSigla")
	@Column(name = "SIGLA", length = 2 )
	private String sigla;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PAIS")
	@Label("lblPaisEstado")
	@NotNull(message="notnull")
	private Pais pais;

	public Estado() {
		super();
	}
	
	public Estado( Long id ) {
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
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
		if (!(obj instanceof Estado))
			return false;
		Estado other = (Estado) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if ( !id.equals(other.getId() ) )
			return false;
		return true;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
