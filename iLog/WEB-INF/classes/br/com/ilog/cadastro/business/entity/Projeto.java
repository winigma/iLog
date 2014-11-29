package br.com.ilog.cadastro.business.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

/**
 * 
 * @author Heber Santiago
 * @Descricao Classe de mapeamento da tabela projetos
 * 
 */
@Entity
@Table( name = "projeto" )	
public class Projeto implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -8034891607191825519L;

	@Id
	@SequenceGenerator( name = "gen", sequenceName = "projeto_id_seq" )
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NOME")
	@NotNull(message="notnull")
	@NotEmpty
	@Label("lblNome")
	private String nome;
	
	@Column(name = "ATIVO")
	@Label("lblAtivo")
	private Boolean ativo;

	public Projeto() {
		super();
	}
	
	public Projeto( Long id ) {
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

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Projeto))
			return false;
		Projeto other = (Projeto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
}
