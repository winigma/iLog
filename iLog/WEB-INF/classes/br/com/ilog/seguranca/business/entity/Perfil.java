package br.com.ilog.seguranca.business.entity;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.EntidadeRepetida;
import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;

@Entity
@Table(name="PERFIL")
@SequenceGenerator( allocationSize = 1, name = "gen", sequenceName = "perfil_id_seq" )
public class Perfil implements Identificavel<Long> {

	/** */
	private static final long serialVersionUID = -1906779131945858036L;

	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	@Column( name = "ID" )
	private Long id;
	
	@Column(name="NOME",nullable = false, length = 300,unique=true)
	@NotNull(message="notnull") @NotEmpty @Label("lblPerfilNome")	
	private String nome;
	
	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.REFRESH)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name="PERFIL_FUNC",
			   joinColumns=@JoinColumn(name="PERFIL_ID"),
			   inverseJoinColumns=@JoinColumn(name="FUNCIONALIDADE_ID"))
	@EntidadeRepetida @Label("lblPerfilRoles")
	private List<Funcionalidade> perfilFuncionalidades;
	
	
	@Override
	public Long getPK() {
		return id;
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


	public List<Funcionalidade> getPerfilFuncionalidades() {
		return perfilFuncionalidades;
	}


	public void setPerfilFuncionalidades(List<Funcionalidade> perfilFuncionalidades) {
		this.perfilFuncionalidades = perfilFuncionalidades;
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
		if (!(obj instanceof Perfil))
			return false;
		Perfil other = (Perfil) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
