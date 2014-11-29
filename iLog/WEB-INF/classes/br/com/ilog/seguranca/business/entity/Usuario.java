package br.com.ilog.seguranca.business.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.Email;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

import br.cits.commons.citsbusiness.hibernate.validator.EntidadeRepetida;
import br.cits.commons.citsbusiness.hibernate.validator.Label;
import br.cits.commons.citsbusiness.util.Identificavel;
import br.com.ilog.cadastro.business.entity.PessoaJuridica;

@Entity
@Table(name="USUARIO")
@SequenceGenerator( allocationSize = 1, name = "gen", sequenceName = "usuario_id_seq" )
public class Usuario implements Identificavel<Long> {
	
	/** */
	private static final long serialVersionUID = 8359757573805638814L;
	
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "gen" )
	@Column(name="id")	
	private Long id;

	@Column(name="login",nullable = false, length = 30,unique=true)
	@NotNull(message="notnull") @Label("lblUsuarioLogin")
	@NotEmpty
	private String login;

	@Column(name="nome",nullable = false, length = 300)	
	@NotNull(message="notnull") @Label("lblUsuarioNome")
	@NotEmpty
	private String nome;

	@Column(name="password",nullable = false, length = 50)
	@NotNull(message="notnull") @Label("lblUsuarioPassword")
	@NotEmpty
	private String password;
	
	@Column(name="email",nullable = true, length = 100 , unique=true)
	@Email @Label("lblUsuarioEmail")
	@NotEmpty
	private String email;
	
	@Column(name="status" , length=20)
	@Enumerated(EnumType.STRING)
	private StatusUsuario status;
	
	@Column(name="bloqueado",nullable=false)
	private boolean bloqueado;
	
	@Column( name = "tipo_usuario", length = 30 )
	@Enumerated( EnumType.STRING )
	@Label("lblTipoUsuario")
	private TipoUsuario tipo;
	
	@JoinColumn(name = "ID_PESSOA_JURIDICA", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private PessoaJuridica pessoaJuridica;
	
	@ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.REFRESH)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name="USUARIO_PERFIL",
			   joinColumns=@JoinColumn(name="USUARIO_ID"),
			   inverseJoinColumns=@JoinColumn(name="PERFIL_ID"))
	@EntidadeRepetida @Label("lblUsuarioPerfis")
	@OrderBy( "nome" )
	private List<Perfil> perfis;
	
	public Usuario() {
		super();
	}
	public Usuario(Long id) {
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


	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login.trim();
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public StatusUsuario getStatus() {
		return status;
	}

	public void setStatus(StatusUsuario status) {
		this.status = status;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}


	public List<Perfil> getPerfis() {
		return perfis;
	}


	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}


	public TipoUsuario getTipo() {
		return tipo;
	}


	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Usuario))
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
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
	/**
	 * @return the pessoaJuridica
	 */
	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}
	/**
	 * @param pessoaJuridica the pessoaJuridica to set
	 */
	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}
}
