package br.com.ilog.seguranca.business.entity;

/**
 * @author Heber Santiago
 * @data 20/06/2011
 *
 * @brief Filtro de Usuario
 */
public class BasicFiltroUsuario {

	private String login;
	
	private String nome ;
	
	private String email;
	
	private StatusUsuario status;
	
	private TipoUsuario tipo;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public TipoUsuario getTipo() {
		return tipo;
	}

	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}
	
}
